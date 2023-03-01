// code by jph
package ch.alpine.sophus.crv;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Dimensions;
import ch.alpine.tensor.fft.Fourier;

class LanczosCurveTest {
  @Test
  void testSimple() {
    Tensor refine = LanczosCurve.refine(Tensors.vector(1, 2, 3, 2, 1, 2, 3), 100);
    assertEquals(refine.length(), 101);
  }

  @Test
  void testMatrix() {
    for (int n = 2; n < 10; ++n) {
      Tensor refine = LanczosCurve.refine(Fourier.FORWARD.matrix(n), 50);
      assertEquals(Dimensions.of(refine), Arrays.asList(51, n));
    }
  }
}
