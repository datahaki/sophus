// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.flt.CenterFilter;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.itp.BinaryAverage;

public enum GeodesicMeanFilter {
  ;
  /** @param binaryAverage
   * @param radius non-negative
   * @return */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, int radius) {
    return CenterFilter.of(GeodesicMean.of(binaryAverage), radius);
  }
}
