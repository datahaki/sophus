// code by jph
package ch.alpine.sophus.hs;

import java.io.Serializable;

import ch.alpine.tensor.Tensor;

@FunctionalInterface
public interface Sedarim extends Serializable {
  /** @param point
   * @return vector of coefficients that indicated how given point
   * relates to points in scattered set */
  Tensor sunder(Tensor point);
}
