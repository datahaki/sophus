// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.id.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;

/** barycentric coordinates for inverse distance weights using distance measurements
 * 
 * @see AbsoluteCoordinate
 * @see InverseDistanceWeighting */
public final class HsInverseDistanceCoordinate extends HsProjection implements ProjectedCoordinate {
  /** @param flattenLogManifold
   * @param weightingInterface that maps a sequence and a point to a vector, for instance the inverse distances */
  public static ProjectedCoordinate custom(FlattenLogManifold flattenLogManifold, WeightingInterface weightingInterface) {
    return new HsInverseDistanceCoordinate(flattenLogManifold, Objects.requireNonNull(weightingInterface));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;

  private HsInverseDistanceCoordinate(FlattenLogManifold flattenLogManifold, WeightingInterface weightingInterface) {
    super(flattenLogManifold);
    this.weightingInterface = weightingInterface;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = weightingInterface.weights(sequence, point);
    return NormalizeAffine.fromNullspace(target, nullsp);
  }
}
