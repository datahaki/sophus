// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.api.SpecificManifold;

public interface SpecificLieGroup extends LieGroup, SpecificManifold {
  int matrixOrder();
}
