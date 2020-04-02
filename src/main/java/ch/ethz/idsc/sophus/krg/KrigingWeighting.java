// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.BarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.sophus.math.id.InverseDistanceWeighting;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Careful: Kriging weights fall not in the category of generalized barycentric
 * coordinates, because Kriging Weighting does not reproduce linear functions!
 * 
 * @see InverseDistanceWeighting */
public class KrigingWeighting implements BarycentricCoordinate, Serializable {
  private final ScalarUnaryOperator variogram;

  public KrigingWeighting(ScalarUnaryOperator variogram) {
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return Krigings.LOGNORM.barycentric(RnManifold.INSTANCE, variogram, sequence).estimate(point);
  }
}
