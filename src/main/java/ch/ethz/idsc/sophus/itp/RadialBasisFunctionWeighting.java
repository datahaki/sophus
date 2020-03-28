// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.id.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;

/** Careful: Radial Basis Function weights fall not in the category of generalized barycentric
 * coordinates, because Radial Basis Function Weighting does not reproduce linear functions!
 * 
 * @see InverseDistanceWeighting */
public class RadialBasisFunctionWeighting implements BarycentricCoordinate, Serializable {
  private final TensorNorm tensorNorm;

  public RadialBasisFunctionWeighting(TensorNorm tensorNorm) {
    this.tensorNorm = Objects.requireNonNull(tensorNorm);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return RadialBasisFunctionInterpolation.barycentric(tensorNorm, sequence).apply(point);
  }
}
