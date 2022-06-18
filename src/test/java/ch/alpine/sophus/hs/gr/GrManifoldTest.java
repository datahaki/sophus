// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.GardenBiinvariant;
import ch.alpine.sophus.dv.LeveragesBiinvariant;
import ch.alpine.sophus.dv.MetricBiinvariant;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;

class GrManifoldTest {
  @Test
  void testMidpoint() {
    int n = 4;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      GrExponential exp_p = new GrExponential(p);
      GrExponential exp_q = new GrExponential(q);
      Tensor m1 = exp_p.midpoint(q);
      Tensor m2 = exp_q.midpoint(p);
      Chop._08.requireClose(m1, m2);
      Tensor m3 = GrManifold.INSTANCE.midpoint(p, q);
      Chop._08.requireClose(m1, m3);
    }
  }

  @Test
  void testMirror() {
    int n = 4;
    for (int k = 1; k < n; ++k) {
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      ScalarTensorFunction stf = GrManifold.INSTANCE.curve(p, q);
      Tensor mir1 = stf.apply(RealScalar.ONE.negate());
      GrExponential exp_p = new GrExponential(p);
      Tensor mir2 = exp_p.flip(q);
      Chop._08.requireClose(mir1, mir2);
    }
  }

  @Test
  void testBiinvariance() {
    Manifold manifold = GrManifold.INSTANCE;
    Biinvariant[] biinvariants = new Biinvariant[] { //
        new MetricBiinvariant(manifold), //
        new LeveragesBiinvariant(manifold), //
        new GardenBiinvariant(manifold) };
    Random random = new Random();
    int n = 3 + random.nextInt(2);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    int k = 1 + random.nextInt(n - 1);
    RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
    int d = k * (n - k);
    Tensor seq_o = RandomSample.of(randomSampleInterface, random, d + 2);
    Tensor pnt_o = RandomSample.of(randomSampleInterface, random);
    for (Biinvariant biinvariant : biinvariants) {
      Tensor w_o = biinvariant.coordinate(variogram, seq_o).sunder(pnt_o);
      GrAction grAction = new GrAction(RandomSample.of(SoRandomSample.of(n), random));
      Tensor seq_l = Tensor.of(seq_o.stream().map(grAction));
      Tensor pnt_l = grAction.apply(pnt_o);
      Tensor w_l = biinvariant.coordinate(variogram, seq_l).sunder(pnt_l);
      Chop._06.requireClose(w_o, w_l);
    }
  }

  @Test
  void testCommute() {
    int n = 5;
    int k = 2;
    RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Tolerance.CHOP.requireClose(p.dot(q), Transpose.of(q.dot(p)));
    Tolerance.CHOP.requireClose(p, p.dot(p));
    Tolerance.CHOP.requireClose(q, q.dot(q));
  }

  @Test
  void testMismatch() {
    int n = 5;
    Tensor p1 = RandomSample.of(new GrRandomSample(n, 1));
    Tensor p2 = RandomSample.of(new GrRandomSample(n, 2));
    Tensor q = ConstantArray.of(Pi.VALUE, n, n);
    assertThrows(Exception.class, () -> new GrExponential(p1).log(q));
    assertThrows(Exception.class, () -> new GrExponential(p2).log(q));
  }

  /** @param vector
   * @return matrix that projects points to line spanned by vector */
  private static Tensor projection1(Tensor vector) {
    return projection(Tensor.of(vector.stream().map(Tensors::of)));
  }

  /** @param design matrix
   * @return */
  private static Tensor projection(Tensor design) {
    return InfluenceMatrix.of(design).matrix();
  }

  @Test
  void testSimple() {
    Tensor log = MatrixLog.of(IdentityMatrix.of(3));
    assertEquals(log, Array.zeros(3, 3));
  }

  @Test
  void testLog() {
    Tensor log = MatrixLog.of(Tensors.fromString("{{1, 0.1}, {0.2, 1}}"));
    Chop._10.requireClose(log, //
        Tensors.fromString("{{-0.01010135365876013, 0.10067478275975056}, {0.20134956551950084, -0.01010135365875986}}"));
  }

  @Test
  void testDistance2d() {
    Tensor p = projection1(Tensors.vector(0.2, 0.5));
    Tensor q = projection1(Tensors.vector(0.3, -0.1));
    Scalar distance = GrManifold.INSTANCE.distance(p, q);
    Chop._10.requireClose(distance, RealScalar.of(2.138348187726219));
  }

  @Test
  void testDistance3d() {
    Tensor x = Tensors.vector(0.2, 0.5, 0.1);
    Tensor y = Tensors.vector(0.3, -0.1, 1.4);
    Tensor p = projection1(x);
    Tensor q = projection1(y);
    Scalar distance = GrManifold.INSTANCE.distance(p, q);
    Chop._10.requireClose(distance, RealScalar.of(1.9499331103710236));
  }

  @Test
  void testRandomSymmetry() {
    RandomSampleInterface randomSampleInterface = new GrRandomSample(4, 3);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Scalar d1 = GrManifold.INSTANCE.distance(p, q);
    Scalar d2 = GrManifold.INSTANCE.distance(q, p);
    Tolerance.CHOP.requireClose(d1, d2);
  }

  @Test
  void testFrobenius() {
    RandomSampleInterface randomSampleInterface = new GrRandomSample(4, 3);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Scalar d1 = GrManifold.INSTANCE.distance(p, q);
    Scalar d2 = FrobeniusNorm.of(new GrExponential(p).log(q));
    Chop._08.requireClose(d1, d2);
  }

  @Test
  void testAntipodal() {
    Tensor p = DiagonalMatrix.of(1, 0);
    Tensor q = DiagonalMatrix.of(0, 1);
    GrMemberQ.INSTANCE.require(p);
    GrMemberQ.INSTANCE.require(q);
    Scalar d1 = GrManifold.INSTANCE.distance(p, q);
    d1.zero();
    Scalar d2 = LowerVectorize0_2Norm.INSTANCE.norm(new GrExponential(p).vectorLog(q));
    Tolerance.CHOP.requireClose(d1, d2);
    // TODO SOPHUS GR check distance of "antipodal" frames, why is this zero?
    // System.out.println(distance);
  }
}
