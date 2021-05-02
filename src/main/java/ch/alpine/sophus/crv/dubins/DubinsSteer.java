// code by jph
package ch.alpine.sophus.crv.dubins;

import java.util.Optional;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

@FunctionalInterface
/* package */ interface DubinsSteer {
  /** @param dist_tr non-negative
   * @param th_tr in the interval [0, 2*pi)
   * @param th_total in the interval [0, 2*pi)
   * @param radius positive
   * @return vector with 3 entries as length of dubins path segments */
  Optional<Tensor> steer(Scalar dist_tr, Scalar th_tr, Scalar th_total, Scalar radius);
}
