// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.itp.BinaryAverage;

/** filter blends extrapolated value with measurement
 * 
 * finite impulse response */
public enum GeodesicFIR2 {
  ;
  /** @param binaryAverage
   * @param alpha
   * @return */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage, Scalar alpha) {
    TensorUnaryOperator geodesicExtrapolation = tensor -> binaryAverage.split(tensor.get(0), tensor.get(1), RealScalar.TWO);
    return new GeodesicFIRn(geodesicExtrapolation, binaryAverage, 2, alpha);
  }
}
