// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class HsProjection implements ProjectionInterface, Serializable {
  protected final FlattenLogManifold flattenLogManifold;

  /** @param flattenLogManifold non-null */
  public HsProjection(FlattenLogManifold flattenLogManifold) {
    this.flattenLogManifold = Objects.requireNonNull(flattenLogManifold);
  }

  @Override // from ProjectionInterface
  public final Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    return PseudoInverse.of(nullsp).dot(nullsp);
  }
}
