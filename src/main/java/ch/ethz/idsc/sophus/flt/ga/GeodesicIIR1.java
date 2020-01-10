// code by jph
package ch.ethz.idsc.sophus.flt.ga;

import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.BinaryAverage;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.Clips;
import ch.ethz.idsc.tensor.sca.Sign;

/** filter blends extrapolated value with measurement */
public class GeodesicIIR1 implements TensorUnaryOperator {
  private final BinaryAverage binaryAverage;
  private final Scalar alpha;
  // ---
  private transient Tensor p = null;

  /** larger alpha means more emphasis on the recent value
   * smaller alpha means more emphasis towards past values
   * 
   * alpha == 1 means that the recent value becomes the filter value
   * 
   * alpha == 1 - remains^(1/steps) with remains after steps
   * 
   * @param binaryAverage
   * @param alpha in the semi-open interval (0, 1] */
  public GeodesicIIR1(BinaryAverage binaryAverage, Scalar alpha) {
    this.binaryAverage = binaryAverage;
    this.alpha = Clips.unit().requireInside(Sign.requirePositive(alpha));
  }

  @Override
  public Tensor apply(Tensor tensor) {
    p = Objects.isNull(p) //
        ? tensor.copy()
        : binaryAverage.split(p, tensor, alpha);
    return p.copy();
  }
}
