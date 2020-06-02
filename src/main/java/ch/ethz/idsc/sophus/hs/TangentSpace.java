// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;

@FunctionalInterface
public interface TangentSpace {
  /** @param q point on manifold
   * @return vector in tangent space */
  Tensor vectorLog(Tensor q);
}
