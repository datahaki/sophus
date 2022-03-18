// code by jph
package ch.alpine.sophus.math.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.win.BartlettWindow;
import ch.alpine.tensor.sca.win.HannWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;

public class HalfWindowSamplerTest {
  @Test
  public void testSimple() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> function = HalfWindowSampler.of(smoothingKernel.get());
      for (int count = 1; count <= 10; ++count) {
        Tensor tensor = function.apply(count);
        assertEquals(tensor.length(), count);
        AffineQ.require(tensor, Chop._08);
      }
    }
  }

  @Test
  public void testSpecific() {
    Function<Integer, Tensor> function = HalfWindowSampler.of(BartlettWindow.FUNCTION);
    assertEquals(function.apply(1), Tensors.fromString("{1}"));
    assertEquals(function.apply(2), Tensors.fromString("{1/3, 2/3}"));
    assertEquals(function.apply(3), Tensors.fromString("{1/6, 1/3, 1/2}"));
  }

  @Test
  public void testExact() {
    Function<Integer, Tensor> halfWindowSampler = HalfWindowSampler.of(HannWindow.FUNCTION);
    Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(HannWindow.FUNCTION);
    for (int length = 1; length < 3; ++length) {
      Tensor tensor = halfWindowSampler.apply(length);
      Tensor expect = NormalizeTotal.FUNCTION.apply(uniformWindowSampler.apply(length * 2 - 1).extract(0, length));
      assertEquals(tensor, expect);
    }
  }

  @Test
  public void testNumeric() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> halfWindowSampler = HalfWindowSampler.of(smoothingKernel.get());
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel.get());
      for (int length = 1; length < 8; ++length) {
        Tensor tensor = halfWindowSampler.apply(length);
        Tensor expect = NormalizeTotal.FUNCTION.apply(uniformWindowSampler.apply(length * 2 - 1).extract(0, length));
        Chop._12.requireClose(tensor, expect);
      }
    }
  }

  @Test
  public void testMemo() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> function = HalfWindowSampler.of(smoothingKernel.get());
      for (int count = 1; count <= 5; ++count) {
        Tensor val1 = function.apply(count);
        Tensor val2 = function.apply(count);
        assertTrue(val1 == val2); // equal by reference
        AssertFail.of(() -> val1.set(RealScalar.ZERO, 0));
      }
    }
  }

  @Test
  public void testZeroFail() {
    Function<Integer, Tensor> function = HalfWindowSampler.of(HannWindow.FUNCTION);
    AssertFail.of(() -> function.apply(0));
  }
}
