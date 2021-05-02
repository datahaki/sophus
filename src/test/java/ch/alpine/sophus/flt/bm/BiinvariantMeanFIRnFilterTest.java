// code by jph
package ch.alpine.sophus.flt.bm;

import ch.alpine.sophus.crv.spline.MonomialExtrapolationMask;
import ch.alpine.sophus.flt.WindowSideExtrapolation;
import ch.alpine.sophus.lie.rn.RnBiinvariantMean;
import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.win.WindowFunctions;
import junit.framework.TestCase;

public class BiinvariantMeanFIRnFilterTest extends TestCase {
  public void testSimple() {
    for (int radius = 0; radius < 6; ++radius) {
      TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanFIRnFilter.of( //
          RnBiinvariantMean.INSTANCE, MonomialExtrapolationMask.INSTANCE, RnGeodesic.INSTANCE, radius, RationalScalar.HALF);
      Tensor signal = Range.of(0, 10);
      Tensor tensor = tensorUnaryOperator.apply(signal);
      assertEquals(signal, tensor);
      ExactTensorQ.require(tensor);
    }
  }

  public void testKernel() {
    for (WindowFunctions smoothingKernel : WindowFunctions.values())
      for (int radius = 0; radius < 6; ++radius) {
        TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanFIRnFilter.of( //
            RnBiinvariantMean.INSTANCE, WindowSideExtrapolation.of(smoothingKernel.get()), RnGeodesic.INSTANCE, radius, RationalScalar.HALF);
        Tensor signal = Range.of(0, 10);
        Tensor tensor = tensorUnaryOperator.apply(signal);
        Chop._10.requireClose(tensor, signal);
      }
  }
}