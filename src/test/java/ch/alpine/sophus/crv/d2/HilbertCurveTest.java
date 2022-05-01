// code by jph
package ch.alpine.sophus.crv.d2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;

public class HilbertCurveTest {
  @Test
  public void testSimple() {
    ExactTensorQ.require(HilbertCurve.of(0));
    for (int n = 1; n < 4; ++n) {
      ExactTensorQ.require(HilbertCurve.of(n));
      ExactTensorQ.require(HilbertPolygon.of(n));
    }
  }

  @Test
  public void testSmall() {
    assertEquals(HilbertCurve.of(0), Tensors.fromString("{{1, 1}}"));
  }

  @Test
  public void testNegativeFail() {
    assertThrows(Exception.class, () -> HilbertCurve.of(-1));
    assertThrows(Exception.class, () -> HilbertPolygon.of(-1));
  }
}
