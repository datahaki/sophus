// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm2Squared;
import ch.ethz.idsc.tensor.sca.Sign;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Quotes:
 * "Multiquadrics are said to be less sensitive to the choice of r0 than some other
 * functional forms."
 * 
 * "In general, r0 should be larger than the typical separation of points but smaller than
 * the 'outer scale' or feature size of the function that you are interpolating. There can
 * be several orders of magnitude difference between the interpolation accuracy with a good
 * choice for r0, versus a poor choice, so it is definitely worth some experimentation. One
 * way to experiment is to construct an RBF interpolator omitting one data point at a time
 * and measuring the interpolation error at the omitted point."
 * 
 * Reference:
 * "Radial Basis Functions in General Use", eq (3.7.5)
 * in NR, 2007 */
public class MultiquadricNorm implements TensorNorm, Serializable {
  private final Scalar r0_squared;

  /** @param r0 non-negative */
  public MultiquadricNorm(Scalar r0) {
    r0_squared = Sign.requirePositiveOrZero(r0).multiply(r0);
  }

  @Override // from TensorNorm
  public Scalar norm(Tensor tensor) {
    return Sqrt.FUNCTION.apply(r0_squared.add(Norm2Squared.ofVector(tensor)));
  }
}
