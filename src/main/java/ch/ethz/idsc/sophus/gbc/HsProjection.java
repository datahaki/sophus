// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;

/** Reference:
 * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
 * by Jan Hakenberg, 2020 */
public class HsProjection implements ProjectionInterface, Serializable {
  protected final FlattenLogManifold flattenLogManifold;

  /** @param flattenLogManifold non-null */
  public HsProjection(FlattenLogManifold flattenLogManifold) {
    this.flattenLogManifold = Objects.requireNonNull(flattenLogManifold);
  }

  public final Tensor levers(Tensor sequence, Tensor point) {
    return Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
  }

  @Override // from ProjectionInterface
  public final Tensor projection(Tensor sequence, Tensor point) {
    Tensor levers = levers(sequence, point);
    Tensor nullsp = LeftNullSpace.usingQR(levers);
    return Transpose.of(nullsp).dot(nullsp);
  }
}
