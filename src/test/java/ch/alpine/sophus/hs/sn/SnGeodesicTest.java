// code by jph
package ch.alpine.sophus.hs.sn;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.hs.s2.S2Geodesic;
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
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;

class SnGeodesicTest {
  @Test
  void testSimple() {
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
}
