// code by jph
package ch.alpine.sophus.math.sample;

import java.util.Random;

import ch.alpine.sophus.hs.r3s2.R3S2Geodesic;
import ch.alpine.sophus.hs.sn.SnManifold;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityMagnitude;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
import junit.framework.TestCase;

public class BallRandomSampleTest extends TestCase {
  public void testSimple() {
    Tensor center = Tensors.vector(10, 20, 30, 40);
    Scalar radius = RealScalar.of(2);
    RandomSampleInterface rsi = BallRandomSample.of(center, radius);
    Tensor vector = RandomSample.of(rsi).subtract(center);
    assertTrue(Scalars.lessEquals(Vector2Norm.of(vector), radius));
  }

  public void test1D() {
    Tensor center = Tensors.vector(15);
    Scalar radius = RealScalar.of(3);
    RandomSampleInterface randomSampleInterface = BallRandomSample.of(center, radius);
    for (int index = 0; index < 100; ++index) {
      Tensor tensor = RandomSample.of(randomSampleInterface);
      VectorQ.requireLength(tensor, 1);
      Clips.interval(12, 18).requireInside(tensor.Get(0));
    }
  }

  public void testSimple2D() {
    RandomSampleInterface diskRandomSample = BallRandomSample.of(Tensors.vector(0, 0), RealScalar.ONE);
    for (int count = 0; count < 100; ++count) {
      Tensor loc = RandomSample.of(diskRandomSample);
      Scalar rad = Vector2Norm.of(loc);
      assertTrue(Scalars.lessEquals(rad, RealScalar.ONE));
    }
  }

  public void testQuantity2D() {
    RandomSampleInterface diskRandomSample = BallRandomSample.of(Tensors.fromString("{10[m], 20[m]}"), Quantity.of(2, "m"));
    Tensor tensor = RandomSample.of(diskRandomSample);
    ScalarUnaryOperator scalarUnaryOperator = QuantityMagnitude.SI().in("m");
    tensor.map(scalarUnaryOperator);
  }

  public void test3DZeroRadius() {
    Tensor center = Tensors.vector(10, 20, 3);
    Scalar radius = RealScalar.of(0.0);
    RandomSampleInterface randomSampleInterface = BallRandomSample.of(center, radius);
    assertEquals(RandomSample.of(randomSampleInterface), center);
  }

  public void testQuantity() {
    RandomSampleInterface randomSampleInterface = //
        BallRandomSample.of(Tensors.fromString("{10[m], 20[m], -5[m]}"), Quantity.of(2, "m"));
    Tensor tensor = RandomSample.of(randomSampleInterface);
    ScalarUnaryOperator scalarUnaryOperator = QuantityMagnitude.SI().in("m");
    tensor.map(scalarUnaryOperator);
  }

  public void testR3S2Geodesic() {
    Random random = new Random(7);
    RandomSampleInterface randomSampleInterface = //
        BallRandomSample.of(Tensors.vector(0, 0, 0), RealScalar.ONE);
    for (int index = 0; index < 20; ++index) {
      Tensor pn = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface, random));
      Tensor qn = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface, random));
      Tensor p = Tensors.of(pn, pn);
      Tensor q = Tensors.of(qn, qn);
      Tensor split = R3S2Geodesic.INSTANCE.split(p, q, RationalScalar.HALF);
      Chop._08.requireClose(split.get(0), split.get(1));
    }
  }

  public void testRotationMatrix3D() {
    for (int index = 0; index < 50; ++index) {
      RandomSampleInterface randomSampleInterface = //
          BallRandomSample.of(Tensors.vector(0, 0, 0), RealScalar.ONE);
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomSample.of(randomSampleInterface));
      Tensor tensor = SnManifold.INSTANCE.endomorphism(p, q);
      Chop._10.requireClose(tensor.dot(p), q);
      assertTrue(OrthogonalMatrixQ.of(tensor, Chop._10));
    }
  }

  public void testLarge() {
    RandomSampleInterface randomSampleInterface = //
        BallRandomSample.of(Array.zeros(10), RealScalar.ONE);
    RandomSample.of(randomSampleInterface);
  }

  public void testCenterEmptyFail() {
    AssertFail.of(() -> BallRandomSample.of(Tensors.empty(), Quantity.of(2, "m")));
  }

  public void testRadiusNegative2Fail() {
    AssertFail.of(() -> BallRandomSample.of(Tensors.vector(1, 2), RealScalar.of(-1)));
  }

  public void testRadiusNegative3Fail() {
    AssertFail.of(() -> BallRandomSample.of(Tensors.vector(1, 2, 3), RealScalar.of(-1)));
  }

  public void testCenterScalarFail() {
    AssertFail.of(() -> BallRandomSample.of(RealScalar.ONE, RealScalar.ONE));
  }

  public void testCenterScalarZeroFail() {
    AssertFail.of(() -> BallRandomSample.of(RealScalar.ONE, RealScalar.ZERO));
  }
}
