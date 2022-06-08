// code by jph
package ch.alpine.sophus.hs;

import ch.alpine.tensor.Tensor;

// TODO SOPHUS possibly merge
public interface HomogeneousSpaceOld {
  /** @param g
   * @param m
   * @return resulting point in manifold when g acts on point m */
  Tensor action(Tensor g, Tensor m);

  /** @param g
   * @return point in manifold */
  Tensor projection(Tensor g);

  /** @param m
   * @return */
  Tensor lift(Tensor m);
}
