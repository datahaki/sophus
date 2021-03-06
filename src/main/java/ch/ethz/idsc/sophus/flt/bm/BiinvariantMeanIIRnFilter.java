// code by jph
package ch.ethz.idsc.sophus.flt.bm;

import java.util.function.Function;

import ch.ethz.idsc.sophus.bm.BiinvariantMean;
import ch.ethz.idsc.sophus.flt.ga.GeodesicIIRnFilter;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.itp.BinaryAverage;

public enum BiinvariantMeanIIRnFilter {
  ;
  /** @param biinvariantMean
   * @param function
   * @param binaryAverage
   * @param radius
   * @param alpha
   * @return */
  public static TensorUnaryOperator of( //
      BiinvariantMean biinvariantMean, Function<Integer, Tensor> function, //
      BinaryAverage binaryAverage, int radius, Scalar alpha) {
    return GeodesicIIRnFilter.of( //
        BiinvariantMeanExtrapolation.of(biinvariantMean, function), //
        binaryAverage, radius, alpha);
  }
}