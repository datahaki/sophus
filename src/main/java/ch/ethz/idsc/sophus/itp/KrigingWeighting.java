// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;

/** Careful: Kriging weights fall not in the category of generalized barycentric
 * coordinates, because Kriging Weighting does not reproduce linear functions!
 * 
 * @see InverseDistanceWeighting */
public class KrigingWeighting implements BarycentricCoordinate, Serializable {
  private final TensorNorm tensorNorm;

  public KrigingWeighting(TensorNorm tensorNorm) {
    this.tensorNorm = Objects.requireNonNull(tensorNorm);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return KrigingInterpolation.barycentric(tensorNorm, sequence).apply(point);
  }
}
