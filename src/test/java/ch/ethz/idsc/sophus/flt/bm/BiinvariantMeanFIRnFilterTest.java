// code by jph
package ch.ethz.idsc.sophus.flt.bm;

import java.io.IOException;

import ch.ethz.idsc.sophus.crv.spline.MonomialExtrapolationMask;
import ch.ethz.idsc.sophus.flt.TestKernels;
import ch.ethz.idsc.sophus.flt.WindowSideExtrapolation;
import ch.ethz.idsc.sophus.lie.rn.RnBiinvariantMean;
import ch.ethz.idsc.sophus.lie.rn.RnGeodesic;
import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class BiinvariantMeanFIRnFilterTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    for (int radius = 0; radius < 6; ++radius) {
      TensorUnaryOperator tensorUnaryOperator = Serialization.copy(BiinvariantMeanFIRnFilter.of( //
          RnBiinvariantMean.INSTANCE, MonomialExtrapolationMask.INSTANCE, RnGeodesic.INSTANCE, radius, RationalScalar.HALF));
      Tensor signal = Range.of(0, 10);
      Tensor tensor = tensorUnaryOperator.apply(signal);
      assertEquals(signal, tensor);
      ExactTensorQ.require(tensor);
    }
  }

  public void testKernel() throws ClassNotFoundException, IOException {
    for (ScalarUnaryOperator smoothingKernel : TestKernels.values())
      for (int radius = 0; radius < 6; ++radius) {
        TensorUnaryOperator tensorUnaryOperator = Serialization.copy(BiinvariantMeanFIRnFilter.of( //
            RnBiinvariantMean.INSTANCE, WindowSideExtrapolation.of(smoothingKernel), RnGeodesic.INSTANCE, radius, RationalScalar.HALF));
        Tensor signal = Range.of(0, 10);
        Tensor tensor = tensorUnaryOperator.apply(signal);
        Chop._10.requireClose(tensor, signal);
      }
  }
}
