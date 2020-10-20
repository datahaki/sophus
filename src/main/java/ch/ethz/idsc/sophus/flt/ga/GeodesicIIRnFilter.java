// code by ob
package ch.ethz.idsc.sophus.flt.ga;

import java.io.Serializable;
import java.util.function.Supplier;

import ch.ethz.idsc.sophus.flt.CausalFilter;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.itp.BinaryAverage;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public enum GeodesicIIRnFilter {
  ;
  /** @param extrapolation
   * @param binaryAverage
   * @param radius
   * @param alpha
   * @return */
  @SuppressWarnings("unchecked")
  public static TensorUnaryOperator of(TensorUnaryOperator extrapolation, BinaryAverage binaryAverage, int radius, Scalar alpha) {
    return CausalFilter.of( //
        (Supplier<TensorUnaryOperator> & Serializable) //
        () -> new GeodesicIIRn(extrapolation, binaryAverage, radius, alpha));
  }
}