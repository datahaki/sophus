// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;

public interface BiinvariantCoordinate extends BarycentricCoordinate {
  /** @param sequence
   * @param point
   * @return */
  Tensor projection(Tensor sequence, Tensor point);
}
