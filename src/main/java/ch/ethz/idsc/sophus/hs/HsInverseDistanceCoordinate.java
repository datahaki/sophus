// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeAffine;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.sophus.math.win.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;
import ch.ethz.idsc.tensor.mat.PseudoInverse;

/** barycentric coordinates for inverse distance weights using distance measurements
 * 
 * @see HsBarycentricCoordinate
 * @see InverseDistanceWeighting */
public final class HsInverseDistanceCoordinate extends HsProjection implements ProjectedCoordinate {
  /** @param flattenLogManifold
   * @param weightingInterface that maps a sequence and a point to a vector, for instance the inverse distances */
  public static ProjectedCoordinate custom(FlattenLogManifold flattenLogManifold, WeightingInterface weightingInterface) {
    return new HsInverseDistanceCoordinate(flattenLogManifold, weightingInterface);
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;

  private HsInverseDistanceCoordinate(FlattenLogManifold flattenLogManifold, WeightingInterface weightingInterface) {
    super(flattenLogManifold);
    this.weightingInterface = Objects.requireNonNull(weightingInterface);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = Tensor.of(sequence.stream().map(flattenLogManifold.logAt(point)::flattenLog));
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = weightingInterface.weights(sequence, point);
    return NormalizeAffine.of(target, PseudoInverse.of(nullsp), nullsp);
  }
}
