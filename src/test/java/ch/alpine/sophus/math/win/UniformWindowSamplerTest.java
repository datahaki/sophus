// code by jph
package ch.alpine.sophus.math.win;

import java.util.function.Function;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.SymmetricVectorQ;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.GaussianWindow;
import ch.alpine.tensor.sca.win.HannWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;
import junit.framework.TestCase;

public class UniformWindowSamplerTest extends TestCase {
  public void testSimple() {
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

  public void testGaussian() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(GaussianWindow.FUNCTION);
    Tensor apply = function.apply(5);
    Chop._12.requireClose(apply, Tensors.vector( //
        0.08562916395501292, //
        0.24266759672960794, //
        0.34340647863075824, //
        0.24266759672960794, //
        0.08562916395501292));
  }

  public void testMemoUnmodifiable() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> function = UniformWindowSampler.of(smoothingKernel.get());
      for (int count = 1; count < 5; ++count) {
        Tensor val1 = function.apply(count);
        Tensor val2 = function.apply(count);
        assertTrue(val1 == val2); // equal by reference
        AssertFail.of(() -> val1.set(RealScalar.ZERO, 0));
      }
    }
  }

  public void testDirichlet() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(DirichletWindow.FUNCTION);
    Tensor MIDPOINT = Tensors.of(RationalScalar.HALF, RationalScalar.HALF);
    assertEquals(function.apply(2), MIDPOINT);
    ExactTensorQ.require(function.apply(2));
  }

  public void testZeroFail() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(HannWindow.FUNCTION);
    AssertFail.of(() -> function.apply(0));
  }

  public void testFailNull() {
    AssertFail.of(() -> UniformWindowSampler.of(null));
  }
}
