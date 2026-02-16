// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.util.Arrays;
import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.hs.s.Sphere;
import ch.alpine.sophus.lie.so.SoNGroup;
import ch.alpine.sophus.math.AveragingWeights;
import ch.alpine.sophus.math.api.Exponential;
import ch.alpine.sophus.math.api.GeodesicSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.TensorProduct;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import test.wrap.ThrowQ;

class GrManifoldTest {
  @Test
  void testMidpoint() {
    int n = 4;
    for (int k = 1; k < n; ++k) {
      Grassmannian grassmannian = new Grassmannian(n, k);
      Tensor p = RandomSample.of(grassmannian);
      Tensor q = RandomSample.of(grassmannian);
      Tensor m1 = GrManifold.INSTANCE.midpoint(p, q);
      Tensor m2 = GrManifold.INSTANCE.midpoint(q, p);
      Chop._08.requireClose(m1, m2);
    }
  }

  @Test
  void testMirror() {
    int n = 4;
    for (int k = 1; k < n; ++k) {
      Grassmannian grassmannian = new Grassmannian(n, k);
      Tensor p = RandomSample.of(grassmannian);
      Tensor q = RandomSample.of(grassmannian);
      Exponential exponential = grassmannian.exponential(p);
      assumeFalse(ThrowQ.of(() -> exponential.log(q)));
      ScalarTensorFunction stf = GrManifold.INSTANCE.curve(p, q);
      Tensor mir1 = stf.apply(RealScalar.ONE.negate());
      // GrExponential exp_p = new GrExponential(p);
      Tensor mir2 = GrManifold.INSTANCE.flip(p, q);
      Chop._08.requireClose(mir1, mir2);
    }
  }

  @Test
  void testCommute() {
    int n = 5;
    int k = 2;
    RandomSampleInterface randomSampleInterface = new Grassmannian(n, k);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Tolerance.CHOP.requireClose(p.dot(q), Transpose.of(q.dot(p)));
    Tolerance.CHOP.requireClose(p, p.dot(p));
    Tolerance.CHOP.requireClose(q, q.dot(q));
  }

  @Test
  void testMismatch() {
    int n = 5;
    Tensor p1 = RandomSample.of(new Grassmannian(n, 1));
    Tensor p2 = RandomSample.of(new Grassmannian(n, 2));
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
    Random random = new Random(3);
    RandomSampleInterface randomSampleInterface = new Grassmannian(4, 3);
    Tensor p = RandomSample.of(randomSampleInterface, random);
    Tensor q = RandomSample.of(randomSampleInterface, random);
    Scalar d1 = GrManifold.INSTANCE.distance(p, q);
    Scalar d2 = GrManifold.INSTANCE.distance(q, p);
    Tolerance.CHOP.requireClose(d1, d2);
  }

  @Test
  void testFrobenius() {
    RandomSampleInterface randomSampleInterface = new Grassmannian(4, 3);
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
    GrManifold.INSTANCE.isPointQ().require(p);
    GrManifold.INSTANCE.isPointQ().require(q);
    Scalar d1 = GrManifold.INSTANCE.distance(p, q);
    Tolerance.CHOP.requireZero(d1);
  }

  private static final BiinvariantMean BIINVARIANT_MEAN = GrManifold.INSTANCE.biinvariantMean();

  @Test
  void testBiinvariant() {
    Distribution distribution = ExponentialDistribution.of(1);
    RandomSampleInterface randomSampleInterface = new Grassmannian(4, 2); // 4 dimensional
    Scalar maxDist = RealScalar.of(1.4);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor sequence = Tensors.of(p);
    for (int iter = 0; iter < 10; ++iter) {
      Tensor q = RandomSample.of(randomSampleInterface);
      Scalar distance = GrManifold.INSTANCE.distance(p, q);
      if (Scalars.lessThan(distance, maxDist))
        sequence.append(q);
    }
    int n = sequence.length();
    Tensor weights = NormalizeTotal.FUNCTION.apply(AveragingWeights.of(sequence.length()).add(RandomVariate.of(distribution, n)));
    assertThrows(Exception.class, () -> BIINVARIANT_MEAN.mean(sequence, RandomVariate.of(distribution, n)));
    Tensor point = BIINVARIANT_MEAN.mean(sequence, weights);
    GrManifold.INSTANCE.isPointQ().require(point);
    GrManifold.INSTANCE.distance(p, point);
    {
      Tensor g = RandomSample.of(new SoNGroup(4));
      GrAction grAction = new GrAction(g);
      Tensor seq_l = Tensor.of(sequence.stream().map(grAction));
      Tensor pnt_l = BIINVARIANT_MEAN.mean(seq_l, weights);
      Chop._08.requireClose(grAction.apply(point), pnt_l);
    }
  }

  @Test
  void testGeodesic() {
    GeodesicSpace hsGeodesic = GrManifold.INSTANCE;
    RandomSampleInterface randomSampleInterface = new Grassmannian(4, 2); // 4 dimensional
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    ScalarTensorFunction scalarTensorFunction = hsGeodesic.curve(p, q);
    Tensor sequence = Subdivide.of(-1.1, 2.1, 6).maps(scalarTensorFunction);
    for (Tensor point : sequence)
      GrManifold.INSTANCE.isPointQ().require(point);
  }
  // @Test
  // void testFromOToP() {
  // int n = 5;
  // for (int k = 0; k <= n; ++k) {
  // int fk = k;
  // Distribution distribution = UniformDistribution.unit();
  // TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(n, k);
  // Tensor ov = tGr0MemberQ.project(RandomVariate.of(distribution, n, n));
  // Tensor o = DiagonalMatrix.with(Tensors.vector(i -> Boole.of(i < fk), n));
  // RandomSampleInterface randomSampleInterface = Grassmannian.of(n, k);
  // Tensor p = RandomSample.of(randomSampleInterface);
  // TensorUnaryOperator tensorUnaryOperator = GrManifold.INSTANCE.hsTransport().shift(o, p);
  // Tensor pv = tensorUnaryOperator.apply(ov);
  // TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
  // tGrMemberQ.requireMember(pv);
  // }
  // }

  @Test
  void testNonMemberFail() {
    int n = 5;
    for (int k = 1; k < n; ++k) {
      Distribution distribution = UniformDistribution.unit();
      RandomSampleInterface randomSampleInterface = new Grassmannian(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      TensorUnaryOperator tensorUnaryOperator = GrManifold.INSTANCE.hsTransport().shift(p, q);
      Tensor ov = RandomVariate.of(distribution, n, n);
      assertThrows(Exception.class, () -> tensorUnaryOperator.apply(ov));
    }
  }

  @Test
  void testSimple2() {
    int n = 5;
    Tensor x = RandomSample.of(new Grassmannian(n, 3));
    assertEquals(Dimensions.of(x), Arrays.asList(n, n));
    GrManifold.INSTANCE.isPointQ().require(x);
  }

  @Test
  void testVectorProject() {
    for (int n = 1; n < 6; ++n) {
      Tensor normal = RandomSample.of(new Sphere(n));
      Tensor x = TensorProduct.of(normal, normal);
      GrManifold.INSTANCE.isPointQ().require(x);
    }
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> GrManifold.INSTANCE.isPointQ().test(null));
  }
}
