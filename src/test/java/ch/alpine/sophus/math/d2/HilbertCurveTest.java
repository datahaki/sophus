// code by jph
package ch.alpine.sophus.math.d2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class HilbertCurveTest extends TestCase {
  public void testSimple() {
    ExactTensorQ.require(HilbertCurve.of(0));
    for (int n = 1; n < 4; ++n) {
      ExactTensorQ.require(HilbertCurve.of(n));
      ExactTensorQ.require(HilbertPolygon.of(n));
    }
  }

  public void testSmall() {
    assertEquals(HilbertCurve.of(0), Tensors.fromString("{{1, 1}}"));
  }

  public void testNegativeFail() {
    AssertFail.of(() -> HilbertPolygon.of(-1));
  }
}
