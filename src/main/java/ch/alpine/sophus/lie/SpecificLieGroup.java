// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.hs.SpecificHomogeneousSpace;

public interface SpecificLieGroup extends LieGroup, SpecificHomogeneousSpace {
  /** @return
   * @see MatrixAlgebra */
  int matrixOrder();
}
