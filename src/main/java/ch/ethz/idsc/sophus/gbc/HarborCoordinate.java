// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.HarborDistances;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** biinvariant coordinate
 * 
 * @see InversePowerVariogram */
public class HarborCoordinate implements TensorUnaryOperator {
  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new HarborCoordinate(vectorLogManifold, variogram, sequence);
  }

  /***************************************************/
  private final HarborDistances harborDistances;

  private HarborCoordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    harborDistances = HarborDistances.frobenius(vectorLogManifold, variogram, sequence);
  }

  @Override
  public Tensor apply(Tensor point) {
    return harborDistances.biinvariantVector(point).coordinate();
  }
}