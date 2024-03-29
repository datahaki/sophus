// code by jph
package ch.alpine.sophus.math.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.SymmetricVectorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.GaussianWindow;
import ch.alpine.tensor.sca.win.HannWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;

class UniformWindowSamplerTest {
  @Test
  void testSimple() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> function = UniformWindowSampler.of(smoothingKernel.get());
      for (int count = 1; count <= 10; ++count) {
        Tensor tensor = function.apply(count);
        assertEquals(tensor.length(), count);
        SymmetricVectorQ.require(tensor);
        AffineQ.require(tensor, Chop._08);
      }
    }
  }

  @Test
  void testGaussian() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(GaussianWindow.FUNCTION);
    Tensor apply = function.apply(5);
    Chop._12.requireClose(apply, Tensors.vector( //
        0.08562916395501292, //
        0.24266759672960794, //
        0.34340647863075824, //
        0.24266759672960794, //
        0.08562916395501292));
  }

  @Test
  void testMemoUnmodifiable() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> function = UniformWindowSampler.of(smoothingKernel.get());
      for (int count = 1; count < 5; ++count) {
        Tensor val1 = function.apply(count);
        Tensor val2 = function.apply(count);
        assertTrue(val1 == val2); // equal by reference
        assertThrows(Exception.class, () -> val1.set(RealScalar.ZERO, 0));
      }
    }
  }

  @Test
  void testDirichlet() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(DirichletWindow.FUNCTION);
    Tensor MIDPOINT = Tensors.of(RationalScalar.HALF, RationalScalar.HALF);
    assertEquals(function.apply(2), MIDPOINT);
    ExactTensorQ.require(function.apply(2));
  }

  @Test
  void testZeroFail() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(HannWindow.FUNCTION);
    assertThrows(Exception.class, () -> function.apply(0));
  }

  @Test
  void testFailNull() {
    assertThrows(Exception.class, () -> UniformWindowSampler.of(null));
  }
}
