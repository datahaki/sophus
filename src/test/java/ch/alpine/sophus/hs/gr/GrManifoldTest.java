// code by jph
package ch.alpine.sophus.hs.gr;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.dv.AveragingWeights;
import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.HsTransport;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.PoleLadder;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.math.LowerVectorize0_2Norm;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.BasisTransform;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.ex.MatrixLog;
import ch.alpine.tensor.mat.gr.InfluenceMatrix;
import ch.alpine.tensor.nrm.FrobeniusNorm;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.num.Boole;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.ExponentialDistribution;
import ch.alpine.tensor.pdf.c.LogisticDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
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
        Biinvariants.METRIC.ofSafe(manifold), //
        Biinvariants.LEVERAGES.ofSafe(manifold), //
        Biinvariants.GARDEN.ofSafe(manifold) };
    Random random1 = new Random();
    int n = 3 + random1.nextInt(2);
    ScalarUnaryOperator variogram = InversePowerVariogram.of(2);
    int k = 1 + random1.nextInt(n - 1);
    RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
    int d = k * (n - k);
    RandomGenerator random = new Random(1);
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

  private static final BiinvariantMean BIINVARIANT_MEAN = GrManifold.INSTANCE.biinvariantMean(Chop._10);

  @Test
  void testBiinvariant() {
    Distribution distribution = ExponentialDistribution.of(1);
    RandomSampleInterface randomSampleInterface = new GrRandomSample(4, 2); // 4 dimensional
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
    Tensor weights = NormalizeTotal.FUNCTION.apply(AveragingWeights.of(n).add(RandomVariate.of(distribution, n)));
    assertThrows(Exception.class, () -> BIINVARIANT_MEAN.mean(sequence, RandomVariate.of(distribution, n)));
    Tensor point = BIINVARIANT_MEAN.mean(sequence, weights);
    GrMemberQ.INSTANCE.require(point);
    GrManifold.INSTANCE.distance(p, point);
    {
      Tensor g = RandomSample.of(SoRandomSample.of(4));
      GrAction grAction = new GrAction(g);
      Tensor seq_l = Tensor.of(sequence.stream().map(grAction));
      Tensor pnt_l = BIINVARIANT_MEAN.mean(seq_l, weights);
      Chop._08.requireClose(grAction.apply(point), pnt_l);
    }
  }

  @RepeatedTest(10)
  void testGeodesic() {
    GeodesicSpace hsGeodesic = GrManifold.INSTANCE;
    RandomSampleInterface randomSampleInterface = new GrRandomSample(4, 2); // 4 dimensional
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    ScalarTensorFunction scalarTensorFunction = hsGeodesic.curve(p, q);
    Tensor sequence = Subdivide.of(-1.1, 2.1, 6).map(scalarTensorFunction);
    for (Tensor point : sequence)
      GrMemberQ.INSTANCE.require(point);
  }

  public static final HsTransport POLE_LADDER = new PoleLadder(GrManifold.INSTANCE);

  @Test
  void testSimple2() throws ClassNotFoundException, IOException {
    int n = 4;
    RandomSampleInterface randomSampleInterface = new GrRandomSample(n, 2);
    Tensor p = RandomSample.of(randomSampleInterface);
    Tensor q = RandomSample.of(randomSampleInterface);
    Distribution distribution = LogisticDistribution.of(1, 3);
    TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
    Tensor pv = tGrMemberQ.projection(RandomVariate.of(distribution, n, n));
    Tensor log = new GrExponential(p).log(q);
    tGrMemberQ.require(log);
    Tensor qv0 = POLE_LADDER.shift(p, q).apply(pv);
    Tensor qv1 = Serialization.copy(GrManifold.INSTANCE.hsTransport().shift(p, q)).apply(pv);
    new TGrMemberQ(q).require(qv1);
    Chop._08.requireClose(qv0, qv1);
    Tensor match = GrAction.match(p, q);
    Tensor ofForm = BasisTransform.ofForm(pv, match);
    // Tensor qw = GrTransport2.INSTANCE.shift(p, q).apply(pv);
    // System.out.println(Pretty.of(qv.map(Round._3)));
    // System.out.println(Pretty.of(qw.map(Round._3)));
    Chop._08.isClose(qv1, ofForm); // this is not correct
  }

  @Test
  void testFromOToP() {
    int n = 5;
    for (int k = 0; k <= n; ++k) {
      int fk = k;
      Distribution distribution = UniformDistribution.unit();
      TGr0MemberQ tGr0MemberQ = new TGr0MemberQ(n, k);
      Tensor ov = tGr0MemberQ.project(RandomVariate.of(distribution, n, n));
      Tensor o = DiagonalMatrix.with(Tensors.vector(i -> Boole.of(i < fk), n));
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      TensorUnaryOperator tensorUnaryOperator = GrManifold.INSTANCE.hsTransport().shift(o, p);
      Tensor pv = tensorUnaryOperator.apply(ov);
      TGrMemberQ tGrMemberQ = new TGrMemberQ(p);
      tGrMemberQ.require(pv);
    }
  }

  @Test
  void testNonMemberFail() {
    int n = 5;
    for (int k = 1; k < n; ++k) {
      Distribution distribution = UniformDistribution.unit();
      RandomSampleInterface randomSampleInterface = new GrRandomSample(n, k);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      TensorUnaryOperator tensorUnaryOperator = GrManifold.INSTANCE.hsTransport().shift(p, q);
      Tensor ov = RandomVariate.of(distribution, n, n);
      assertThrows(Exception.class, () -> tensorUnaryOperator.apply(ov));
    }
  }
}
