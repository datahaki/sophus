// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.dv.HarborBiinvariantVector;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** biinvariant coordinate
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * @see InversePowerVariogram */
public enum HarborCoordinate {
  ;
  /** @param manifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of( //
      HsDesign hsDesign, ScalarUnaryOperator variogram, Tensor sequence) {
    return new BiinvariantVectorCoordinate(HarborBiinvariantVector.of(hsDesign, sequence), variogram);
  }
}
