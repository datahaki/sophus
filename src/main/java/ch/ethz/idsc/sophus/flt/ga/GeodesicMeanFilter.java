// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import ch.ethz.idsc.sophus.flt.CenterFilter;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.itp.BinaryAverage;

public enum GeodesicMeanFilter {
  ;
  /** @param binaryAverage
   * @param radius non-negative
   * @return */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, int radius) {
    return CenterFilter.of(GeodesicMean.of(binaryAverage), radius);
  }
}
