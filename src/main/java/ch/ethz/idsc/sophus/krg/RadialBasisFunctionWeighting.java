// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.sophus.math.id.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;

/** Careful: Radial Basis Function weights fall not in the category of generalized barycentric
 * coordinates, because Radial Basis Function Weighting does not reproduce linear functions!
 * 
 * @see InverseDistanceWeighting
 * @see Krigings */
public class RadialBasisFunctionWeighting implements WeightingInterface, Serializable {
  /** @param tensorNorm
   * @return */
  public static WeightingInterface of(PseudoDistances pseudoDistances) {
    return new RadialBasisFunctionWeighting(Objects.requireNonNull(pseudoDistances));
  }

  /***************************************************/
  private final PseudoDistances pseudoDistances;

  private RadialBasisFunctionWeighting(PseudoDistances pseudoDistances) {
    this.pseudoDistances = pseudoDistances;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return RadialBasisFunctionInterpolation.barycentric(pseudoDistances, sequence).apply(point);
  }
}
