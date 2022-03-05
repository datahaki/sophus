// code by jph
package ch.alpine.sophus.flt.ga;

import java.util.function.Function;

import ch.alpine.sophus.crv.spline.MonomialExtrapolationMask;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.lie.se2.Se2Geodesic;
import ch.alpine.sophus.math.win.HalfWindowSampler;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.Sin;
import ch.alpine.tensor.sca.win.DirichletWindow;
import ch.alpine.tensor.sca.win.GaussianWindow;
import ch.alpine.tensor.sca.win.WindowFunctions;
import junit.framework.TestCase;

public class GeodesicExtrapolationTest extends TestCase {
  public void testEmptyFail() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGeodesic.INSTANCE, DirichletWindow.FUNCTION);
    AssertFail.of(() -> tensorUnaryOperator.apply(Tensors.empty()));
  }

  public void testDirichlet() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGeodesic.INSTANCE, DirichletWindow.FUNCTION);
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

  public void testMonomial() {
    TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGeodesic.INSTANCE, MonomialExtrapolationMask.INSTANCE);
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

  public void testSimple() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGeodesic.INSTANCE, smoothingKernel.get());
      for (int index = 2; index < 10; ++index) {
        Chop._12.requireClose(tensorUnaryOperator.apply(Range.of(0, index)), RealScalar.of(index));
      }
    }
  }

  public void testSingle() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values()) {
      TensorUnaryOperator tensorUnaryOperator = GeodesicExtrapolation.of(RnGeodesic.INSTANCE, smoothingKernel.get());
      Chop._12.requireClose(tensorUnaryOperator.apply(Tensors.vector(10)), RealScalar.of(10));
    }
  }

  public void testSplits2() {
    Tensor mask = Tensors.vector(0.5, 0.5);
    Tensor result = GeodesicExtrapolation.Splits.of(mask);
    assertEquals(Tensors.vector(2), result);
  }

  public void testElaborate() {
    Function<Integer, Tensor> halfWindowSampler = HalfWindowSampler.of(GaussianWindow.FUNCTION);
    Tensor mask = halfWindowSampler.apply(7);
    assertEquals(mask.length(), 7);
    Tensor result = GeodesicExtrapolation.Splits.of(mask);
    Tensor expect = Tensors.vector( //
        0.6045315182147757, 0.4610592079176246, 0.3765618899029577, //
        0.3135075836053491, 0.2603421497384919, 1.3568791242517575);
    Chop._12.requireClose(expect, result);
  }

  public void testNoExtrapolation() {
    Tensor mask = Tensors.vector(1);
    Tensor result = GeodesicExtrapolation.Splits.of(mask);
    assertEquals(Tensors.vector(1), result);
  }

  public void testAffinityFail() {
    Tensor mask = Tensors.vector(0.5, 0.8);
    AssertFail.of(() -> GeodesicExtrapolation.Splits.of(mask));
  }

  public void testNullFail1() {
    AssertFail.of(() -> GeodesicExtrapolation.of(null, Sin.FUNCTION));
  }

  public void testNullFailITF() {
    AssertFail.of(() -> GeodesicExtrapolation.of(Se2Geodesic.INSTANCE, (Function<Integer, Tensor>) null));
  }

  public void testNullFailSUO() {
    AssertFail.of(() -> GeodesicExtrapolation.of(Se2Geodesic.INSTANCE, (ScalarUnaryOperator) null));
  }
}
