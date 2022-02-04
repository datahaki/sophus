// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class CurvatureCombTest extends TestCase {
  public void testSimple() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}");
    Tensor tensor = CurvatureComb.of(points, RealScalar.ONE.negate(), false);
    String string = "{{-0.7071067811865474, 0.7071067811865474}, {1, 2}, {2.7071067811865475, 0.7071067811865474}}";
    Tensor result = Tensors.fromString(string);
    Chop._12.requireClose(tensor, result);
  }

  public void testStringLength() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 10; ++count) {
      Tensor tensor = RandomVariate.of(distribution, count, 2);
      Tensor string = CurvatureComb.string(tensor);
      assertEquals(string.length(), count);
    }
  }

  public void testCircle() {
    Tensor tensor = CurvatureComb.of(CirclePoints.of(4), RealScalar.ONE.negate(), true);
    Chop._14.requireClose(tensor, CirclePoints.of(4).multiply(RealScalar.of(2)));
  }

  public void testString() {
    Tensor tensor = CurvatureComb.string(Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}"));
    String format = "{{-0.7071067811865474, 0.7071067811865474}, {0, 1}, {0.7071067811865474, 0.7071067811865474}}";
    Tensor result = Tensors.fromString(format).negate();
    Chop._12.requireClose(tensor, result);
  }

  public void testEmpty() {
    assertTrue(Tensors.isEmpty(CurvatureComb.of(Tensors.empty(), RealScalar.of(2), true)));
    assertTrue(Tensors.isEmpty(CurvatureComb.of(Tensors.empty(), RealScalar.of(2), false)));
    assertEquals(CurvatureComb.cyclic(Tensors.empty()), Tensors.empty());
  }

  public void testOne() {
    Tensor tensor = CurvatureComb.of(Tensors.fromString("{{1, 2}}"), RealScalar.of(2), false);
    assertEquals(tensor, Tensors.fromString("{{1, 2}}"));
  }

  public void testTwo() {
    Tensor tensor = CurvatureComb.of(Tensors.fromString("{{1, 2}, {4, 5}}"), RealScalar.of(2), false);
    assertEquals(tensor, Tensors.fromString("{{1, 2}, {4, 5}}"));
  }

  public void testZeros() {
    Tensor tensor = Array.zeros(10, 2);
    assertEquals(tensor, CurvatureComb.of(tensor, RealScalar.of(2), false));
    assertEquals(tensor, CurvatureComb.of(tensor, RealScalar.of(2), true));
  }

  public void testFail() {
    Tensor points = Tensors.fromString("{{0, 0, 0}, {1, 1, 0}, {2, 0, 0}}");
    AssertFail.of(() -> CurvatureComb.of(points, RealScalar.ONE, false));
  }
}
