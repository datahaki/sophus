// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.function.Function;

import ch.ethz.idsc.sophus.flt.TestKernels;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.SymmetricVectorQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.win.GaussianWindow;
import ch.ethz.idsc.tensor.sca.win.HannWindow;
import junit.framework.TestCase;

public class UniformWindowSamplerTest extends TestCase {
  public void testSimple() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> function = UniformWindowSampler.of(smoothingKernel);
      for (int count = 1; count <= 10; ++count) {
        Tensor tensor = function.apply(count);
        assertEquals(tensor.length(), count);
        SymmetricVectorQ.require(tensor);
        AffineQ.require(tensor);
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
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> function = UniformWindowSampler.of(smoothingKernel);
      for (int count = 1; count < 5; ++count) {
        Tensor val1 = function.apply(count);
        Tensor val2 = function.apply(count);
        assertTrue(val1 == val2); // equal by reference
        try {
          val1.set(RealScalar.ZERO, 0);
          fail();
        } catch (Exception exception) {
          // ---
        }
      }
    }
  }

  public void testZeroFail() {
    Function<Integer, Tensor> function = UniformWindowSampler.of(HannWindow.FUNCTION);
    AssertFail.of(() -> function.apply(0));
  }

  public void testFailNull() {
    AssertFail.of(() -> UniformWindowSampler.of(null));
  }
}
