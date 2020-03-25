// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.Tensor;

public interface PseudoDistances {
  /** @param point
   * @return vector of pseudo distances between given point to each point from a fixed sequence of points */
  Tensor pseudoDistances(Tensor point);
}
