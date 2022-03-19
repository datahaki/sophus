// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Chop;

public class CurvatureCombTest {
  @Test
  public void testSimple() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}");
    Tensor tensor = CurvatureComb.of(points, RealScalar.ONE.negate(), false);
    String string = "{{-0.7071067811865474, 0.7071067811865474}, {1, 2}, {2.7071067811865475, 0.7071067811865474}}";
    Tensor result = Tensors.fromString(string);
    Chop._12.requireClose(tensor, result);
  }

  @Test
  public void testCirclePoints() {
    for (int n = 3; n < 10; ++n)
      Tolerance.CHOP.requireAllZero(Total.of(CurvatureComb.cyclic(CirclePoints.of(n))));
  }

  @Test
  public void testStringLength() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, count, 2);
      Tensor string = CurvatureComb.string(tensor);
      assertEquals(string.length(), count);
    }
  }

  @Test
  public void testCircle() {
    Tensor tensor = CurvatureComb.of(CirclePoints.of(4), RealScalar.ONE.negate(), true);
    Chop._14.requireClose(tensor, CirclePoints.of(4).multiply(RealScalar.of(2)));
  }

  @Test
  public void testString() {
    Tensor tensor = CurvatureComb.string(Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}"));
    String format = "{{-0.7071067811865474, 0.7071067811865474}, {0, 1}, {0.7071067811865474, 0.7071067811865474}}";
    Tensor result = Tensors.fromString(format).negate();
    Chop._12.requireClose(tensor, result);
  }

  @Test
  public void testEmpty() {
    assertTrue(Tensors.isEmpty(CurvatureComb.of(Tensors.empty(), RealScalar.of(2), true)));
    assertTrue(Tensors.isEmpty(CurvatureComb.of(Tensors.empty(), RealScalar.of(2), false)));
    assertEquals(CurvatureComb.cyclic(Tensors.empty()), Tensors.empty());
  }

  @Test
  public void testOne() {
    Tensor tensor = CurvatureComb.of(Tensors.fromString("{{1, 2}}"), RealScalar.of(2), false);
    assertEquals(tensor, Tensors.fromString("{{1, 2}}"));
  }

  @Test
  public void testTwo() {
    Tensor tensor = CurvatureComb.of(Tensors.fromString("{{1, 2}, {4, 5}}"), RealScalar.of(2), false);
    assertEquals(tensor, Tensors.fromString("{{1, 2}, {4, 5}}"));
  }

  @Test
  public void testZeros() {
    Tensor tensor = Array.zeros(10, 2);
    assertEquals(tensor, CurvatureComb.of(tensor, RealScalar.of(2), false));
    assertEquals(tensor, CurvatureComb.of(tensor, RealScalar.of(2), true));
  }

  @Test
  public void testQuantity() {
    Distribution distribution = NormalDistribution.of(Quantity.of(0, "m"), Quantity.of(1, "m"));
    Tensor p = RandomVariate.of(distribution, 2);
    Tensor q = RandomVariate.of(distribution, 2);
    Tensor r = RandomVariate.of(distribution, 2);
    Tensor tangent = p.subtract(r);
    Tensor t1 = CurvatureComb.normal(p, q, r, tangent);
    Tensor t2 = CurvatureComb.normal(p, p, p, tangent);
    t1.add(t2);
    assertEquals(t2, Tensors.fromString("{0[m^-1], 0[m^-1]}"));
  }

  @Test
  public void testFail() {
    Tensor points = Tensors.fromString("{{0, 0, 0}, {1, 1, 0}, {2, 0, 0}}");
    assertThrows(Exception.class, () -> CurvatureComb.of(points, RealScalar.ONE, false));
  }
}
