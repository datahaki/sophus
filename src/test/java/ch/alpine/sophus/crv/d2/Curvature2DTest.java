// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.QuantityUnit;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

public class Curvature2DTest {
  @Test
  public void testString2() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}}");
    Tensor vector = Curvature2D.string(points);
    Chop._12.requireClose(vector, Tensors.vector(0, 0));
  }

  @Test
  public void testString3() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}");
    Tensor vector = Curvature2D.string(points);
    Chop._12.requireClose(vector, Tensors.vector(-1, -1, -1));
  }

  @Test
  public void testStringEmpty() {
    Tensor points = Tensors.empty();
    Tensor vector = Curvature2D.string(points);
    assertEquals(points, vector);
  }

  @Test
  public void testQuantity() {
    Tensor points = Join.of(CirclePoints.of(10), Array.zeros(10, 2)).map(s -> Quantity.of(s, "m"));
    Tensor string = Curvature2D.string(points);
    assertEquals(string.stream().map(Scalar.class::cast).map(QuantityUnit::of).distinct().count(), 1);
  }

  @Test
  public void testQuantity3() {
    Tensor points = CirclePoints.of(3).map(s -> Quantity.of(s, "m"));
    Tensor string = Curvature2D.string(points);
    VectorQ.requireLength(string, 3);
    assertEquals(string.stream().map(Scalar.class::cast).map(QuantityUnit::of).distinct().count(), 1);
  }

  @Test
  public void testQuantity4() {
    Tensor points = CirclePoints.of(4).map(s -> Quantity.of(s, "m"));
    Tensor string = Curvature2D.string(points);
    VectorQ.requireLength(string, 4);
    assertEquals(string.stream().map(Scalar.class::cast).map(QuantityUnit::of).distinct().count(), 1);
  }

  @Test
  public void testCirclePoints() {
    for (int n = 3; n < 10; ++n)
      Tolerance.CHOP.requireClose(Total.of(Curvature2D.string(CirclePoints.of(n))), RealScalar.of(n));
  }

  @Test
  public void testQuantityFail() {
    Tensor points = Join.of(Array.zeros(10, 2).map(s -> Quantity.of(s, "m")), Array.zeros(10, 2));
    assertThrows(Exception.class, () -> Curvature2D.string(points));
  }

  @Test
  public void testFailHi() {
    Tensor points = Tensors.fromString("{{0, 0, 0}, {1, 1, 0}, {2, 0, 0}}");
    assertThrows(Exception.class, () -> Curvature2D.string(points));
  }

  @Test
  public void testFailStringScalar() {
    assertThrows(Exception.class, () -> Curvature2D.string(RealScalar.ZERO));
  }
}
