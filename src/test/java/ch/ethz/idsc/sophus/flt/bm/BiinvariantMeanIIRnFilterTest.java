// code by jph
package ch.ethz.idsc.sophus.flt.bm;

import java.util.function.Function;

import ch.ethz.idsc.sophus.crv.spline.MonomialExtrapolationMask;
import ch.ethz.idsc.sophus.flt.WindowSideExtrapolation;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.win.WindowFunctions;
import junit.framework.TestCase;

public class BiinvariantMeanIIRnFilterTest extends TestCase {
  public void testSimple() {
    for (int radius = 0; radius < 6; ++radius) {
      TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanIIRnFilter.of( //
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
        Function<Integer, Tensor> function = WindowSideExtrapolation.of(smoothingKernel.get());
        TensorUnaryOperator tensorUnaryOperator = BiinvariantMeanIIRnFilter.of( //
            RnBiinvariantMean.INSTANCE, function, RnGeodesic.INSTANCE, radius, RationalScalar.HALF);
        Tensor signal = Range.of(0, 10);
        Tensor tensor = tensorUnaryOperator.apply(signal);
        Chop._10.requireClose(tensor, signal);
      }
  }
}
