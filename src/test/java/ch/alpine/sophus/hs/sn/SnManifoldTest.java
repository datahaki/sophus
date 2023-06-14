// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.dv.AveragingWeights;
import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.GbcHelper;
import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.r2.ArcTan2D;
import ch.alpine.sophus.hs.s2.S2Geodesic;
import ch.alpine.sophus.lie.so.SoRandomSample;
import ch.alpine.sophus.lie.so3.Rodrigues;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.sample.RandomSample;
import ch.alpine.sophus.math.sample.RandomSampleInterface;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.r2.AngleVector;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.ArcCos;

class SnManifoldTest {
  private static final BarycentricCoordinate[] BARYCENTRIC_COORDINATES = //
      GbcHelper.barycentrics(SnManifold.INSTANCE);

  private static Tensor randomCloud(Tensor mean, int n, Random random) {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    return Tensor.of(RandomVariate.of(distribution, random, n, mean.length()).stream().map(mean::add).map(Vector2Norm.NORMALIZE));
  }

  @Test
  void testLinearReproduction() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 5; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(mean, n, random);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
        Chop._06.requireAllZero(evaluate);
        Chop._06.requireClose(mean, SnManifold.INSTANCE.biinvariantMean(Chop._14).mean(sequence, weights));
      }
  }

  @Test
  void testLagrangeProperty() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 4; ++d) {
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(UnitVector.of(d, 0), n, random);
        int count = random.nextInt(sequence.length());
        Tensor mean = sequence.get(count);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        Chop._06.requireClose(weights, UnitVector.of(n, count));
        Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
        Chop._06.requireAllZero(evaluate);
        Chop._03.requireClose(mean, SnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights));
      }
  }

  @Test
  void testBiinvariance() {
    Random random = new Random(3);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES)
      for (int d = 2; d < 5; ++d) {
        Tensor mean = UnitVector.of(d, 0);
        RandomSampleInterface randomSampleInterface = SoRandomSample.of(d);
        int n = d + 1 + random.nextInt(3);
        Tensor sequence = randomCloud(mean, n, random);
        Tensor weights = barycentricCoordinate.weights(sequence, mean);
        VectorQ.requireLength(weights, n);
        AffineQ.require(weights, Chop._08);
        {
          Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
          Chop._08.requireAllZero(evaluate);
        }
        // ---
        {
          Tensor matrix = RandomSample.of(randomSampleInterface);
          Tensor mean2 = matrix.dot(mean);
          Tensor shifted = Tensor.of(sequence.stream().map(matrix::dot));
          Tensor evaluate = new MeanDefect(shifted, weights, SnManifold.INSTANCE.exponential(mean2)).tangent();
          Chop._10.requireAllZero(evaluate);
          Tensor weights2 = barycentricCoordinate.weights(shifted, mean2);
          Chop._04.requireClose(weights, weights2); // 1e-6 does not always work
        }
      }
  }

  private static Scalar _check(Tensor p, Tensor q) {
    return ArcCos.FUNCTION.apply((Scalar) p.dot(q)); // complex number if |p.q| > 1
  }

  @Test
  void testSimple() {
    Chop._12.requireClose(SnManifold.INSTANCE.distance(UnitVector.of(3, 0), UnitVector.of(3, 1)), Pi.HALF);
    Chop._12.requireClose(SnManifold.INSTANCE.distance(UnitVector.of(3, 0), UnitVector.of(3, 2)), Pi.HALF);
    Chop._12.requireClose(SnManifold.INSTANCE.distance(UnitVector.of(3, 1), UnitVector.of(3, 2)), Pi.HALF);
  }

  @Test
  void testDirect() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Scalar a1 = SnManifold.INSTANCE.distance(p, q);
      Scalar a2 = _check(p, q);
      Chop._12.requireClose(a1, a2);
      Scalar norm = Vector2Norm.of(new SnExponential(p).log(q));
      Tolerance.CHOP.requireClose(norm, a1);
    }
  }

  @Test
  void testSpecific() {
    Distribution distribution = NormalDistribution.of(0, 0.2);
    for (BarycentricCoordinate barycentricCoordinate : BARYCENTRIC_COORDINATES) {
      Tensor rotation = Rodrigues.vectorExp(RandomVariate.of(distribution, 3));
      Tensor mean = rotation.dot(Vector2Norm.NORMALIZE.apply(Tensors.vector(1, 1, 1)));
      Tensor sequence = Tensor.of(IdentityMatrix.of(3).stream().map(rotation::dot));
      Chop._08.requireClose(sequence, Transpose.of(rotation));
      Tensor weights = barycentricCoordinate.weights(sequence, mean);
      Chop._12.requireClose(weights, NormalizeTotal.FUNCTION.apply(Tensors.vector(1, 1, 1)));
      Tensor evaluate = new MeanDefect(sequence, weights, SnManifold.INSTANCE.exponential(mean)).tangent();
      Chop._12.requireAllZero(evaluate);
      Chop._05.requireClose(mean, SnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights));
    }
  }

  @Test
  void testS1Linear() {
    Distribution distribution = UniformDistribution.of(0, Math.PI);
    for (int n = 2; n < 10; ++n) {
      Tensor angles = RandomVariate.of(distribution, n);
      Tensor sequence = angles.map(AngleVector::of);
      Tensor weights = AveragingWeights.of(n);
      Tensor point = SnManifold.INSTANCE.biinvariantMean(Chop._06).mean(sequence, weights);
      Chop._05.requireClose(ArcTan2D.of(point), Mean.of(angles));
    }
  }

  @Test
  void testSimple2() {
    Tensor p = UnitVector.of(3, 0);
    Tensor q = UnitVector.of(3, 1);
    Tensor split = SnManifold.INSTANCE.split(p, q, RationalScalar.HALF);
    assertEquals(Vector2Norm.of(split), RealScalar.ONE);
    assertEquals(split.Get(0), split.Get(1));
    assertTrue(Scalars.isZero(split.Get(2)));
  }

  @Test
  void test2D() {
    ScalarTensorFunction scalarTensorFunction = //
        SnManifold.INSTANCE.curve(UnitVector.of(2, 0), UnitVector.of(2, 1));
    for (int n = 3; n < 20; ++n) {
      Tensor points = Subdivide.of(0, 4, n).map(scalarTensorFunction);
      Tensor circle = CirclePoints.of(n);
      Chop._12.requireClose(points.extract(0, n), circle);
    }
  }

  @Test
  void test4D() {
    ScalarTensorFunction scalarTensorFunction = //
        SnManifold.INSTANCE.curve(UnitVector.of(4, 0), UnitVector.of(4, 1));
    Tensor ZEROS = Array.zeros(2);
    for (int n = 3; n < 20; ++n) {
      Tensor points = Subdivide.of(0, 4, n).map(scalarTensorFunction);
      Tensor circle = Tensor.of(CirclePoints.of(n).stream().map(t -> Join.of(t, ZEROS)));
      Chop._12.requireClose(points.extract(0, n), circle);
    }
  }

  @Test
  void testRatio() {
    Tensor p = UnitVector.of(3, 0);
    Tensor q = UnitVector.of(3, 1);
    Tensor split2 = S2Geodesic.INSTANCE.split(p, q, RationalScalar.of(1, 3));
    Tensor splitn = SnManifold.INSTANCE.split(p, q, RationalScalar.of(1, 3));
    Chop._12.requireClose(split2, splitn);
  }

  @Test
  void testSame() {
    Tensor p = UnitVector.of(3, 2);
    Tensor q = UnitVector.of(3, 2);
    Tensor split = SnManifold.INSTANCE.split(p, q, RandomVariate.of(NormalDistribution.standard()));
    ExactTensorQ.require(split);
    assertEquals(split, p);
  }

  @Test
  void testOpposite() {
    Tensor p = UnitVector.of(3, 2);
    Tensor q = UnitVector.of(3, 2).negate();
    Scalar scalar = RandomVariate.of(NormalDistribution.standard());
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.split(p, q, scalar));
  }

  @Test
  void testComparison() {
    Distribution distribution = NormalDistribution.standard();
    GeodesicSpace hsGeodesic = SnManifold.INSTANCE;
    for (int index = 0; index < 10; ++index) {
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Scalar scalar = RandomVariate.of(distribution);
      Tensor split2 = S2Geodesic.INSTANCE.split(p, q, scalar);
      Tensor splitn = SnManifold.INSTANCE.split(p, q, scalar);
      Tensor splith = hsGeodesic.split(p, q, scalar);
      Chop._10.requireClose(split2, splitn);
      Chop._10.requireClose(split2, splith);
    }
  }

  @Test
  void testEndPoints() {
    Distribution distribution = NormalDistribution.standard();
    for (int index = 0; index < 10; ++index) {
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Chop._14.requireClose(p, SnManifold.INSTANCE.split(p, q, RealScalar.ZERO));
      Tensor r = SnManifold.INSTANCE.split(p, q, RealScalar.ONE);
      Chop._12.requireClose(q, r);
      Chop._14.requireClose(Vector2Norm.of(r), RealScalar.ONE);
    }
  }

  @Test
  void testArticle() {
    Tensor p = Tensors.vector(1, 0, 0);
    Tensor q = Tensors.vector(0, 1 / Math.sqrt(5), 2 / Math.sqrt(5));
    Tensor tensor = SnManifold.INSTANCE.split(p, q, RealScalar.of(0.4));
    // in sync with Mathematica
    Tensor expect = Tensors.vector(0.8090169943749473, 0.2628655560595668, 0.5257311121191336);
    Chop._12.requireClose(tensor, expect);
  }

  @Test
  void testMidpoint() {
    GeodesicSpace hsMidpoint = SnManifold.INSTANCE;
    for (int dimension = 2; dimension <= 5; ++dimension) {
      RandomSampleInterface randomSampleInterface = SnRandomSample.of(dimension);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor m1 = SnManifold.INSTANCE.midpoint(p, q);
      Tensor m2 = hsMidpoint.midpoint(p, q);
      Chop._08.requireClose(m1, m2);
    }
  }

  @Test
  void testDimensionsFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.split(UnitVector.of(4, 0), UnitVector.of(3, 1), RealScalar.ZERO));
  }

  @Test
  void testNormFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.split(Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), RationalScalar.HALF));
  }

  @Test
  void testMidpointAntipodesFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.midpoint(UnitVector.of(3, 0), UnitVector.of(3, 0).negate()));
  }

  @Test
  void testMemberQFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.distance(Tensors.vector(1, 0), Tensors.vector(1, 1)));
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.distance(Tensors.vector(1, 1), Tensors.vector(1, 0)));
  }
}
