// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.TargetDistances;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** target coordinate is identical to anchor coordinate
 * 
 * @see AnchorCoordinate */
public class TargetCoordinate implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public static BarycentricCoordinate of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new TargetCoordinate(vectorLogManifold, variogram);
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;
  private final HsProjection hsProjection;

  private TargetCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    weightingInterface = TargetDistances.of(vectorLogManifold, variogram);
    hsProjection = new HsProjection(vectorLogManifold);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    Tensor weights = NormalizeTotal.FUNCTION.apply(weightingInterface.weights(sequence, point));
    return NormalizeTotal.FUNCTION.apply(hsProjection.projection(sequence, point).dot(weights));
  }
}
