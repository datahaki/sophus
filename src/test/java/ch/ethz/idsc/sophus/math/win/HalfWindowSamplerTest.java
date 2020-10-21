// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.function.Function;

import ch.ethz.idsc.sophus.flt.TestKernels;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.win.BartlettWindow;
import ch.ethz.idsc.tensor.sca.win.HannWindow;
import junit.framework.TestCase;

public class HalfWindowSamplerTest extends TestCase {
  public void testSimple() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> function = HalfWindowSampler.of(smoothingKernel);
      for (int count = 1; count <= 10; ++count) {
        Tensor tensor = function.apply(count);
        assertEquals(tensor.length(), count);
        AffineQ.require(tensor);
      }
    }
  }

  public void testSpecific() {
    Function<Integer, Tensor> function = HalfWindowSampler.of(BartlettWindow.FUNCTION);
    assertEquals(function.apply(1), Tensors.fromString("{1}"));
    assertEquals(function.apply(2), Tensors.fromString("{1/3, 2/3}"));
    assertEquals(function.apply(3), Tensors.fromString("{1/6, 1/3, 1/2}"));
  }

  public void testExact() {
    Function<Integer, Tensor> halfWindowSampler = HalfWindowSampler.of(HannWindow.FUNCTION);
    Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(HannWindow.FUNCTION);
    for (int length = 1; length < 3; ++length) {
      Tensor tensor = halfWindowSampler.apply(length);
      Tensor expect = NormalizeTotal.FUNCTION.apply(uniformWindowSampler.apply(length * 2 - 1).extract(0, length));
      assertEquals(tensor, expect);
      ExactTensorQ.require(tensor);
      ExactTensorQ.require(expect);
    }
  }

  public void testNumeric() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> halfWindowSampler = HalfWindowSampler.of(smoothingKernel);
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel);
      for (int length = 1; length < 8; ++length) {
        Tensor tensor = halfWindowSampler.apply(length);
        Tensor expect = NormalizeTotal.FUNCTION.apply(uniformWindowSampler.apply(length * 2 - 1).extract(0, length));
        Chop._12.requireClose(tensor, expect);
      }
    }
  }

  public void testMemo() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> function = HalfWindowSampler.of(smoothingKernel);
      for (int count = 1; count <= 5; ++count) {
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
    Function<Integer, Tensor> function = HalfWindowSampler.of(HannWindow.FUNCTION);
    AssertFail.of(() -> function.apply(0));
  }
}
