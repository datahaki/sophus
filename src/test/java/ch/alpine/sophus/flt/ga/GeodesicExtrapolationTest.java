// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.function.Function;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.crv.MonomialExtrapolationMask;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.sophus.lie.se2.Se2Group;
import ch.alpine.sophus.math.win.HalfWindowSampler;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.ExactTensorQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.Sin;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.GaussianWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;

class GeodesicExtrapolationTest {
  @Test
  void testEmptyFail() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGroup.INSTANCE, DirichletWindow.FUNCTION);
    assertThrows(Exception.class, () -> tensorUnaryOperator.apply(Tensors.empty()));
  }

  @Test
  void testDirichlet() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGroup.INSTANCE, DirichletWindow.FUNCTION);
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(12));
      assertEquals(tensor, RealScalar.of(12));
      ExactTensorQ.require(tensor);
    }
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2));
      assertEquals(tensor, RealScalar.of(3));
      ExactTensorQ.require(tensor);
    }
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
      assertEquals(tensor, RealScalar.of(4));
      ExactTensorQ.require(tensor);
    }
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2, 1));
      assertEquals(tensor, RationalScalar.of(2, 3));
      ExactTensorQ.require(tensor);
    }
  }

  @Test
  void testMonomial() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGroup.INSTANCE, MonomialExtrapolationMask.INSTANCE);
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(12));
      assertEquals(tensor, RealScalar.of(12));
      ExactTensorQ.require(tensor);
    }
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2));
      assertEquals(tensor, RealScalar.of(3));
      ExactTensorQ.require(tensor);
    }
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2, 3));
      assertEquals(tensor, RealScalar.of(4));
      ExactTensorQ.require(tensor);
    }
    {
      Tensor tensor = tensorUnaryOperator.apply(Tensors.vector(1, 2, 1));
      assertEquals(tensor, RationalScalar.of(-2, 1));
      ExactTensorQ.require(tensor);
    }
  }

  @Test
  void testSimple() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGroup.INSTANCE, smoothingKernel.get());
      for (int index = 2; index < 10; ++index) {
        Chop._12.requireClose(tensorUnaryOperator.apply(Range.of(0, index)), RealScalar.of(index));
      }
    }
  }

  @Test
  void testSingle() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGroup.INSTANCE, smoothingKernel.get());
      Tolerance.CHOP.requireClose(tensorUnaryOperator.apply(Tensors.vector(10)), RealScalar.of(10));
    }
  }

  @Test
  void testSplits2() {
    Tensor mask = Tensors.vector(0.5, 0.5);
    Tensor result = GeodesicExtrapolation.Splits.of(mask);
    assertEquals(Tensors.vector(2), result);
  }

  @Test
  void testElaborate() {
    Function<Integer, Tensor> halfWindowSampler = HalfWindowSampler.of(GaussianWindow.FUNCTION);
    Tensor mask = halfWindowSampler.apply(7);
    assertEquals(mask.length(), 7);
    Tensor result = GeodesicExtrapolation.Splits.of(mask);
    Tensor expect = Tensors.vector( //
        0.6045315182147757, 0.4610592079176246, 0.3765618899029577, //
        0.3135075836053491, 0.2603421497384919, 1.3568791242517575);
    Tolerance.CHOP.requireClose(expect, result);
  }

  @Test
  void testNoExtrapolation() {
    Tensor mask = Tensors.vector(1);
    Tensor result = GeodesicExtrapolation.Splits.of(mask);
    assertEquals(Tensors.vector(1), result);
  }

  @Test
  void testAffinityFail() {
    Tensor mask = Tensors.vector(0.5, 0.8);
    assertThrows(Exception.class, () -> GeodesicExtrapolation.Splits.of(mask));
  }

  @Test
  void testNullFail1() {
    assertThrows(Exception.class, () -> GeodesicExtrapolation.of(null, Sin.FUNCTION));
  }

  @Test
  void testNullFailITF() {
    assertThrows(Exception.class, () -> GeodesicExtrapolation.of(Se2Group.INSTANCE, (Function<Integer, Tensor>) null));
  }

  @Test
  void testNullFailSUO() {
    assertThrows(Exception.class, () -> GeodesicExtrapolation.of(Se2Group.INSTANCE, (ScalarUnaryOperator) null));
  }
}
