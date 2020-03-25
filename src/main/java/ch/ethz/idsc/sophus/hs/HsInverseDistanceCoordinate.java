// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** barycentric coordinates for inverse distance weights
 * using distance measurements
 * 
 * @see HsBiinvariantCoordinate */
public class HsInverseDistanceCoordinate implements BarycentricCoordinate, Serializable {
  private final FlattenLogManifold flattenLogManifold;
  private final BarycentricCoordinate barycentricCoordinate;

  /** @param barycentricCoordinate that maps a sequence and a point to a vector, for instance the inverse distances */
  public HsInverseDistanceCoordinate(FlattenLogManifold flattenLogManifold, BarycentricCoordinate target) {
    this.flattenLogManifold = Objects.requireNonNull(flattenLogManifold);
    this.barycentricCoordinate = Objects.requireNonNull(target);
  }

  @Override // from BarycentricCoordinate
  public final Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = barycentricCoordinate.weights(sequence, point);
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }
}
