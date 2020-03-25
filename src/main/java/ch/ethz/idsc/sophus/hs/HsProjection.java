// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.win.ProjectionInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

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
