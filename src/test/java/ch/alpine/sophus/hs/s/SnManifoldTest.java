// code by jph
package ch.alpine.sophus.hs.s;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.GeodesicSpace;
import ch.alpine.sophus.lie.so2.ArcTan2D;
import ch.alpine.sophus.math.AveragingWeights;
import ch.alpine.tensor.Rational;
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
import ch.alpine.tensor.lie.rot.AngleVector;
import ch.alpine.tensor.lie.rot.CirclePoints;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.red.Mean;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.ArcCos;

class SnManifoldTest {
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
  void testS1Linear() {
    Distribution distribution = UniformDistribution.of(0, Math.PI);
    for (int n = 2; n < 10; ++n) {
      Tensor angles = RandomVariate.of(distribution, n);
      Tensor sequence = angles.maps(AngleVector::of);
      Tensor weights = AveragingWeights.of(sequence.length());
      Tensor point = SnManifold.INSTANCE.biinvariantMean().mean(sequence, weights);
      Chop._05.requireClose(ArcTan2D.of(point), Mean.of(angles));
    }
  }

  @Test
  void testSimple2() {
    Tensor p = UnitVector.of(3, 0);
    Tensor q = UnitVector.of(3, 1);
    Tensor split = SnManifold.INSTANCE.split(p, q, Rational.HALF);
    assertEquals(Vector2Norm.of(split), RealScalar.ONE);
    assertEquals(split.Get(0), split.Get(1));
    assertTrue(Scalars.isZero(split.Get(2)));
  }

  @Test
  void test2D() {
    ScalarTensorFunction scalarTensorFunction = //
        SnManifold.INSTANCE.curve(UnitVector.of(2, 0), UnitVector.of(2, 1));
    for (int n = 3; n < 20; ++n) {
      Tensor points = Subdivide.of(0, 4, n).maps(scalarTensorFunction);
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
      Tensor points = Subdivide.of(0, 4, n).maps(scalarTensorFunction);
      Tensor circle = Tensor.of(CirclePoints.of(n).stream().map(t -> Join.of(t, ZEROS)));
      Chop._12.requireClose(points.extract(0, n), circle);
    }
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
      RandomSampleInterface randomSampleInterface = new Sphere(dimension);
      Tensor p = RandomSample.of(randomSampleInterface);
      Tensor q = RandomSample.of(randomSampleInterface);
      Tensor m1 = SnManifold.INSTANCE.midpoint(p, q);
      Tensor m2 = hsMidpoint.midpoint(p, q);
      Chop._08.requireClose(m1, m2);
    }
  }

  @Test
  void testD1() {
    SnManifold.INSTANCE.isPointQ().require(Tensors.vector(+1));
    SnManifold.INSTANCE.isPointQ().require(Tensors.vector(-1));
  }

  @Test
  void testD2() {
    for (int n = 5; n < 14; ++n)
      CirclePoints.of(n).forEach(SnManifold.INSTANCE.isPointQ()::require);
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.isPointQ().test(null));
  }

  @Test
  void testDimensionsFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.split(UnitVector.of(4, 0), UnitVector.of(3, 1), RealScalar.ZERO));
  }

  @Test
  void testNormFail() {
    assertThrows(Exception.class, () -> SnManifold.INSTANCE.split(Tensors.vector(1, 2, 3), Tensors.vector(4, 5, 6), Rational.HALF));
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
