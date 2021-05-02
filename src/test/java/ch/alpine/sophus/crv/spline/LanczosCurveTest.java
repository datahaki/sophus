// code by jph
package ch.alpine.sophus.crv.spline;

import java.util.Arrays;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.fft.FourierMatrix;
import junit.framework.TestCase;

public class LanczosCurveTest extends TestCase {
  public void testSimple() {
    Tensor refine = LanczosCurve.refine(Tensors.vector(1, 2, 3, 2, 1, 2, 3), 100);
    assertEquals(refine.length(), 101);
  }

  public void testMatrix() {
    for (int n = 2; n < 10; ++n) {
      Tensor refine = LanczosCurve.refine(FourierMatrix.of(n), 50);
      assertEquals(Dimensions.of(refine), Arrays.asList(51, n));
    }
  }
}
