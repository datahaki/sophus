// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsLevers;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeftNullSpace;

/** barycentric coordinates for inverse distance weights using distance measurements
 * 
 * @see AbsoluteCoordinate */
public final class HsInverseDistanceCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param weightingInterface that maps a sequence and a point to a vector, for instance the inverse distances */
  public static BarycentricCoordinate custom(VectorLogManifold vectorLogManifold, WeightingInterface weightingInterface) {
    return new HsInverseDistanceCoordinate(vectorLogManifold, Objects.requireNonNull(weightingInterface));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;
  private final HsLevers hsLevers;

  private HsInverseDistanceCoordinate(VectorLogManifold vectorLogManifold, WeightingInterface weightingInterface) {
    hsLevers = new HsLevers(vectorLogManifold);
    this.weightingInterface = weightingInterface;
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor levers = hsLevers.levers(sequence, point);
    Tensor nullsp = LeftNullSpace.of(levers);
    Tensor target = weightingInterface.weights(sequence, point);
    return NormalizeAffine.fromNullspace(target, nullsp);
  }
}
