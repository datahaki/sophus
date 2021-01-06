// code by jph
package ch.ethz.idsc.sophus.math.win;

import java.util.function.Function;

import ch.ethz.idsc.sophus.flt.TestKernels;
import ch.ethz.idsc.sophus.math.SymmetricVectorQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.win.DirichletWindow;
import ch.ethz.idsc.tensor.sca.win.HannWindow;
import junit.framework.TestCase;

public class BaseWindowSamplerTest extends TestCase {
  private static Tensor constant(int i) {
    int width = 2 * i + 1;
    Scalar weight = RationalScalar.of(1, width);
    return Tensors.vector(k -> weight, width);
  }

  public void testConstant() {
    Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(DirichletWindow.FUNCTION);
    for (int radius = 0; radius < 5; ++radius) {
      Tensor tensor = uniformWindowSampler.apply(radius * 2 + 1);
      assertEquals(tensor, constant(radius));
      ExactTensorQ.require(tensor);
      assertEquals(Total.of(tensor), RealScalar.ONE);
    }
  }

  public void testHann() {
    Function<Integer, Tensor> centerWindowSampler = UniformWindowSampler.of(HannWindow.FUNCTION);
    ExactTensorQ.require(centerWindowSampler.apply(1));
    ExactTensorQ.require(centerWindowSampler.apply(2));
  }

  public void testAll() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel);
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

  public void testSymmetric() {
    for (int size = 0; size < 5; ++size) {
      Tensor tensor = RandomVariate.of(NormalDistribution.standard(), 2, 3, 4);
      for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
        Tensor v1 = tensor.map(smoothingKernel);
        Tensor v2 = tensor.negate().map(smoothingKernel);
        assertEquals(v1, v2);
      }
    }
  }

  public void testContinuity() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Scalar scalar = smoothingKernel.apply(RationalScalar.HALF);
      String string = smoothingKernel.toString().toLowerCase() + "Window[1/2]=" + scalar;
      string.length();
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel);
      Tensor vector = uniformWindowSampler.apply(1);
      assertEquals(vector, Tensors.of(RealScalar.ONE));
      assertTrue(Scalars.lessThan(RealScalar.of(1e-3), Abs.of(vector.Get(0))));
    }
  }

  public void testZeroFail() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> uniformWindowSampler = UniformWindowSampler.of(smoothingKernel);
      AssertFail.of(() -> uniformWindowSampler.apply(0));
    }
  }

  public void testAllFail() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values()) {
      Function<Integer, Tensor> centerWindowSampler = UniformWindowSampler.of(smoothingKernel);
      AssertFail.of(() -> centerWindowSampler.apply(-1));
    }
  }

  public void testAllFailQuantity() {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values())
      AssertFail.of(() -> smoothingKernel.apply(Quantity.of(1, "s")));
  }
}
