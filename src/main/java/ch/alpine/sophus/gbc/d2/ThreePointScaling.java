// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface ThreePointScaling {
  /** BiFunction that takes as input 1) a non-zero vector "dif" and 2) the 2-norm of vector "dif".
   * 
   * @param dif
   * @param nrm == Vector2Norm.of(dif)
   * @return scaled version of dif */
  Tensor scale(Tensor dif, Scalar nrm);
}
