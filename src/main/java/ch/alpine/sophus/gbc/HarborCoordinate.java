// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.dv.HarborBiinvariantVector;
import ch.alpine.sophus.hs.VectorLogManifold;
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
  /** @param vectorLogManifold
   * @param variogram for instance power to minus two
   * @param sequence */
  public static TensorUnaryOperator of( //
      VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new BiinvariantVectorCoordinate(HarborBiinvariantVector.of(vectorLogManifold, sequence), variogram);
  }
}