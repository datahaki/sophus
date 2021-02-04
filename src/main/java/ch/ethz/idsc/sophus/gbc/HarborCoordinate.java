// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.dv.HarborDistanceVector;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** biinvariant coordinate
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see InversePowerVariogram */
public class HarborCoordinate implements TensorUnaryOperator {
  private static final long serialVersionUID = 1837765663914890392L;

  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of( //
      VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new HarborCoordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final HarborDistanceVector harborDistances;
  private final ScalarUnaryOperator variogram;

  private HarborCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    harborDistances = HarborDistanceVector.frobenius(vectorLogManifold, sequence);
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override
  public Tensor apply(Tensor point) {
    return harborDistances.biinvariantVector(point).coordinate(variogram);
  }
}
