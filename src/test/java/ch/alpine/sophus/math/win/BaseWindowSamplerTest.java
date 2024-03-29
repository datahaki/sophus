// code by jph
package ch.alpine.sophus.math.win;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.math.SymmetricVectorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.red.Total;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.HannWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;

class BaseWindowSamplerTest {
  private static Tensor constant(int i) {
    int width = 2 * i + 1;
    Scalar weight = RationalScalar.of(1, width);
    return Tensors.vector(k -> weight, width);
  }

  @Test
  void testConstant() {
    Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(DirichletWindow.FUNCTION);
    for (int radius = 0; radius < 5; ++radius) {
      Tensor tensor = uniformWindowSampler.apply(radius * 2 + 1);
      assertEquals(tensor, constant(radius));
      ExactTensorQ.require(tensor);
      assertEquals(Total.of(tensor), RealScalar.ONE);
    }
  }

  @Test
  void testHann() {
    Function<Integer, Tensor> centerWindowSampler = UniformWindowSampler.of(HannWindow.FUNCTION);
    ExactTensorQ.require(centerWindowSampler.apply(1));
    assertEquals(centerWindowSampler.apply(2), Tensors.vector(0.5, 0.5));
  }

  @Test
  void testAll() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel.get());
      for (int radius = 0; radius < 5; ++radius) {
        Tensor tensor = uniformWindowSampler.apply(radius * 2 + 1);
        SymmetricVectorQ.require(tensor);
        Chop._13.requireClose(Total.of(tensor), RealScalar.ONE);
        assertFalse(Scalars.isZero(tensor.Get(0)));
        assertFalse(Scalars.isZero(tensor.Get(tensor.length() - 1)));
        assertEquals(tensor.length(), 2 * radius + 1);
      }
    }
  }

  @Test
  void testSymmetric() {
    for (int size = 0; size < 5; ++size) {
      Tensor tensor = RandomVariate.of(NormalDistribution.standard(), 2, 3, 4);
      for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
        Tensor v1 = tensor.map(smoothingKernel.get());
        Tensor v2 = tensor.negate().map(smoothingKernel.get());
        assertEquals(v1, v2);
      }
    }
  }

  @Test
  void testContinuity() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Scalar scalar = smoothingKernel.get().apply(RationalScalar.HALF);
      String string = smoothingKernel.get() + "[1/2]=" + scalar;
      string.length();
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel.get());
      Tensor vector = uniformWindowSampler.apply(1);
      assertEquals(vector, Tensors.of(RealScalar.ONE));
      assertTrue(Scalars.lessThan(RealScalar.of(1e-3), Abs.FUNCTION.apply(vector.Get(0))));
    }
  }

  @Test
  void testZeroFail() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel.get());
      assertThrows(Exception.class, () -> uniformWindowSampler.apply(0));
    }
  }

  @Test
  void testAllFail() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      Function<Integer, Tensor> centerWindowSampler = UniformWindowSampler.of(smoothingKernel.get());
      assertThrows(Exception.class, () -> centerWindowSampler.apply(-1));
    }
  }

  @Test
  void testAllFailQuantity() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values())
      assertThrows(Exception.class, () -> smoothingKernel.get().apply(Quantity.of(1, "s")));
  }
}
