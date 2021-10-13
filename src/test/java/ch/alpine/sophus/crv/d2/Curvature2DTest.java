// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityUnit;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Curvature2DTest extends TestCase {
  public void testString2() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}}");
    Tensor vector = Curvature2D.string(points);
    Chop._12.requireClose(vector, Tensors.vector(0, 0));
  }

  public void testString3() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}");
    Tensor vector = Curvature2D.string(points);
    Chop._12.requireClose(vector, Tensors.vector(-1, -1, -1));
  }

  public void testStringEmpty() {
    Tensor points = Tensors.empty();
    Tensor vector = Curvature2D.string(points);
    assertEquals(points, vector);
  }

  public void testQuantity() {
    Tensor points = Join.of(CirclePoints.of(10), Array.zeros(10, 2)).map(s -> Quantity.of(s, "m"));
    Tensor string = Curvature2D.string(points);
    assertEquals(string.stream().map(Scalar.class::cast).map(QuantityUnit::of).distinct().count(), 1);
  }

  public void testQuantity3() {
    Tensor points = CirclePoints.of(3).map(s -> Quantity.of(s, "m"));
    Tensor string = Curvature2D.string(points);
    VectorQ.requireLength(string, 3);
    assertEquals(string.stream().map(Scalar.class::cast).map(QuantityUnit::of).distinct().count(), 1);
  }

  public void testQuantity4() {
    Tensor points = CirclePoints.of(4).map(s -> Quantity.of(s, "m"));
    Tensor string = Curvature2D.string(points);
    VectorQ.requireLength(string, 4);
    assertEquals(string.stream().map(Scalar.class::cast).map(QuantityUnit::of).distinct().count(), 1);
  }

  public void testQuantityFail() {
    Tensor points = Join.of(Array.zeros(10, 2).map(s -> Quantity.of(s, "m")), Array.zeros(10, 2));
    AssertFail.of(() -> Curvature2D.string(points));
  }

  public void testFailHi() {
    Tensor points = Tensors.fromString("{{0, 0, 0}, {1, 1, 0}, {2, 0, 0}}");
    AssertFail.of(() -> Curvature2D.string(points));
  }

  public void testFailStringScalar() {
    AssertFail.of(() -> Curvature2D.string(RealScalar.ZERO));
  }
}
