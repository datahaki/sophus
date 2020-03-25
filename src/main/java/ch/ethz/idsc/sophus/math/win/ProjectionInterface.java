// code by jph
package ch.ethz.idsc.sophus.math.win;

import ch.ethz.idsc.tensor.Tensor;

public interface ProjectionInterface {
  /** @param sequence
   * @param point
   * @return symmetric projection matrix with eigenvalues either 1 or 0 */
  Tensor projection(Tensor sequence, Tensor point);
}
