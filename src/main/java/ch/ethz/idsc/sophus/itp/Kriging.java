// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction (13.6), also known in different communities as Gauss-Markov
 * estimation or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public interface Kriging {
  /** @param point
   * @return estimate at given point */
  Tensor estimate(Tensor point);

  /** @param point
   * @return variance of estimate at given point */
  Scalar variance(Tensor point);
}
