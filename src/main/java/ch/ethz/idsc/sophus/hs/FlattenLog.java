// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface FlattenLog {
  /** @param q point on manifold
   * @return vector in tangent space */
  Tensor flattenLog(Tensor q);
}
