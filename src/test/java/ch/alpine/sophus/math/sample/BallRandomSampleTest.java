// code by jph
package ch.alpine.sophus.math.sample;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Random;
import java.util.random.RandomGenerator;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.r3s2.R3S2Geodesic;
import ch.alpine.sophus.hs.s.SnRotationMatrix;
import ch.alpine.tensor.Rational;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.pdf.RandomSample;
import ch.alpine.tensor.pdf.RandomSampleInterface;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;

class BallRandomSampleTest {
  @Test
  void testSimple1() {
    Tensor tensor = RandomSample.of(BallRandomSample.of(Tensors.vector(1, 2, 3), RealScalar.ONE), 6);
    assertEquals(Dimensions.of(tensor), Arrays.asList(6, 3));
  }

  @Test
  void testSimple() {
    Tensor center = Tensors.vector(10, 20, 30, 40);
    Scalar radius = RealScalar.of(2);
    RandomSampleInterface rsi = BallRandomSample.of(center, radius);
    Tensor vector = RandomSample.of(rsi).subtract(center);
    assertTrue(Scalars.lessEquals(Vector2Norm.of(vector), radius));
  }

  @Test
  void test1D() {
    Tensor center = Tensors.vector(15);
    Scalar radius = RealScalar.of(3);
    RandomSampleInterface randomSampleInterface = BallRandomSample.of(center, radius);
    for (int index = 0; index < 100; ++index) {
      Tensor tensor = RandomSample.of(randomSampleInterface);
      VectorQ.requireLength(tensor, 1);
      Clips.interval(12, 18).requireInside(tensor.Get(0));
    }
  }

  @Test
  void testSimple2D() {
    RandomSampleInterface diskRandomSample = BallRandomSample.of(Tensors.vector(0, 0), RealScalar.ONE);
    for (int count = 0; count < 100; ++count) {
      Tensor loc = RandomSample.of(diskRandomSample);
      Scalar rad = Vector2Norm.of(loc);
      assertTrue(Scalars.lessEquals(rad, RealScalar.ONE));
    }
  }

  @Test
  void testQuantity2D() {
    RandomSampleInterface diskRandomSample = BallRandomSample.of(Tensors.fromString("{10[m], 20[m]}"), Quantity.of(2, "m"));
    Tensor tensor = RandomSample.of(diskRandomSample);
    ScalarUnaryOperator scalarUnaryOperator = QuantityMagnitude.SI().in("m");
    tensor.maps(scalarUnaryOperator);
  }

  @Test
  void test3DZeroRadius() {
    Tensor center = Tensors.vector(10, 20, 3);
    Scalar radius = RealScalar.of(0.0);
    RandomSampleInterface randomSampleInterface = BallRandomSample.of(center, radius);
    assertEquals(RandomSample.of(randomSampleInterface), center);
  }

  @Test
  void testQuantity() {
    RandomSampleInterface randomSampleInterface = //
        BallRandomSample.of(Tensors.fromString("{10[m], 20[m], -5[m]}"), Quantity.of(2, "m"));
    Tensor tensor = RandomSample.of(randomSampleInterface);
    ScalarUnaryOperator scalarUnaryOperator = QuantityMagnitude.SI().in("m");
    tensor.maps(scalarUnaryOperator);
  }

  @Test
  void testR3S2Geodesic() {
    RandomGenerator random = new Random(7);
    RandomSampleInterface randomSampleInterface = //
        BallRandomSample.of(Tensors.vector(0, 0, 0), RealScalar.ONE);
    for (int index = 0; index < 20; ++index) {
      Tensor pn = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface, random));
      Tensor qn = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface, random));
      Tensor p = Tensors.of(pn, pn);
      Tensor q = Tensors.of(qn, qn);
      Tensor split = R3S2Geodesic.INSTANCE.split(p, q, Rational.HALF);
      Chop._08.requireClose(split.get(0), split.get(1));
    }
  }

  @Test
  void testRotationMatrix3D() {
    for (int index = 0; index < 50; ++index) {
      RandomSampleInterface randomSampleInterface = //
          BallRandomSample.of(Tensors.vector(0, 0, 0), RealScalar.ONE);
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface));
      Tensor tensor = SnRotationMatrix.of(p, q);
      Chop._10.requireClose(tensor.dot(p), q);
      assertTrue(new OrthogonalMatrixQ(Chop._10).test(tensor));
    }
  }

  @Test
  void testLarge() {
    RandomSampleInterface randomSampleInterface = //
        BallRandomSample.of(Array.zeros(10), RealScalar.ONE);
    RandomSample.of(randomSampleInterface);
  }

  @Test
  void testCenterEmptyFail() {
    assertThrows(Exception.class, () -> BallRandomSample.of(Tensors.empty(), Quantity.of(2, "m")));
  }

  @Test
  void testRadiusNegative2Fail() {
    assertThrows(Exception.class, () -> BallRandomSample.of(Tensors.vector(1, 2), RealScalar.of(-1)));
  }

  @Test
  void testRadiusNegative3Fail() {
    assertThrows(Exception.class, () -> BallRandomSample.of(Tensors.vector(1, 2, 3), RealScalar.of(-1)));
  }

  @Test
  void testCenterScalarFail() {
    assertThrows(Exception.class, () -> BallRandomSample.of(RealScalar.ONE, RealScalar.ONE));
  }

  @Test
  void testCenterScalarZeroFail() {
    assertThrows(Exception.class, () -> BallRandomSample.of(RealScalar.ONE, RealScalar.ZERO));
  }
}
