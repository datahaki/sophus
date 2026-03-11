// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.api.SpecificManifold;

public interface SpecificLieGroup extends LieGroup, SpecificManifold {
  /** @return
   * @see MatrixAlgebra */
  int matrixOrder();
}
