// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction (13.6), also known in different communities as Gauss-Markov
 * estimation or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public interface Kriging extends TensorUnaryOperator {
  /** @param x
   * @return error estimate */
  Scalar variance(Tensor x);
}
