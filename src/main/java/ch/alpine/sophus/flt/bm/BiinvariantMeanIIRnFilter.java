// code by jph
package ch.alpine.sophus.flt.bm;

import java.util.function.Function;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.flt.ga.GeodesicIIRnFilter;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.BinaryAverage;

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
        new BiinvariantMeanExtrapolation(biinvariantMean, function), //
        binaryAverage, radius, alpha);
  }
}