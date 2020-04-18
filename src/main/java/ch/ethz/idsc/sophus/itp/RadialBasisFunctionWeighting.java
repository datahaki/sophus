// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.krg.Krigings;
import ch.ethz.idsc.sophus.math.TensorNorm;
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
  public static WeightingInterface of(TensorNorm tensorNorm) {
    return new RadialBasisFunctionWeighting(Objects.requireNonNull(tensorNorm));
  }

  /***************************************************/
  private final TensorNorm tensorNorm;

  private RadialBasisFunctionWeighting(TensorNorm tensorNorm) {
    this.tensorNorm = tensorNorm;
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return RadialBasisFunctionInterpolation.barycentric(tensorNorm, sequence).apply(point);
  }
}
