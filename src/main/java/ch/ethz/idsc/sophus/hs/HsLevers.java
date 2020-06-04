// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public final class HsLevers implements Serializable {
  private final VectorLogManifold vectorLogManifold;

  /** @param vectorLogManifold non-null */
  public HsLevers(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = Objects.requireNonNull(vectorLogManifold);
  }

  /** @param sequence
   * @param point
   * @return */
  public Tensor levers(Tensor sequence, Tensor point) {
    return Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog));
  }
}
