// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface FlattenLog {
  /** @param q
   * @return */
  Tensor flattenLog(Tensor q);
  // TODO flattenExp
}
