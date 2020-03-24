// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Careful: Kriging weights fall not in the category of generalized barycentric
 * coordinates, because Kriging Weighting does not reproduce linear functions!
 * 
 * @see InverseDistanceWeighting */
public class KrigingWeighting implements BarycentricCoordinate, Serializable {
  private final ScalarUnaryOperator variogram;
  private final TensorNorm tensorNorm;

  public KrigingWeighting(ScalarUnaryOperator variogram, TensorNorm tensorNorm) {
    this.variogram = Objects.requireNonNull(variogram);
    this.tensorNorm = Objects.requireNonNull(tensorNorm);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return Kriging.barycentric(variogram, tensorNorm, sequence).apply(point);
  }
}
