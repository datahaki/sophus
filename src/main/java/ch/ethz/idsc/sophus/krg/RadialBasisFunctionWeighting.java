// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;

/** Careful: Radial Basis Function weights fall not in the category of generalized barycentric
 * coordinates, because Radial Basis Function Weighting does not reproduce linear functions!
 * 
 * @see Kriging */
public class RadialBasisFunctionWeighting implements WeightingInterface, Serializable {
  /** @param weightingInterface
   * @return */
  public static WeightingInterface of(WeightingInterface weightingInterface) {
    return new RadialBasisFunctionWeighting(Objects.requireNonNull(weightingInterface));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;

  private RadialBasisFunctionWeighting(WeightingInterface weightingInterface) {
    this.weightingInterface = weightingInterface;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return RadialBasisFunctionInterpolation.partitions(weightingInterface, sequence).apply(point);
  }
}
