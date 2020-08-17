// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.HarborDistances;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** biinvariant coordinate
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see InversePowerVariogram */
public class HarborCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of( //
      VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new HarborCoordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final HarborDistances harborDistances;
  private final ScalarUnaryOperator variogram;

  private HarborCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    harborDistances = HarborDistances.frobenius(vectorLogManifold, sequence);
    this.variogram = Objects.requireNonNull(variogram);
  }

  @Override
  public Tensor apply(Tensor point) {
    return harborDistances.biinvariantVector(point).coordinate(variogram);
  }
}
