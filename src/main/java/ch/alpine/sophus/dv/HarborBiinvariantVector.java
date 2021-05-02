// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;

import ch.alpine.sophus.hs.BiinvariantVectorFunction;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.math.TensorMetric;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.nrm.FrobeniusNorm;

/** for Rn and Sn the frobenius distance results in identical coordinates as the 2-norm distance
 * 
 * however, for SE(2) the frobenius and 2-norm coordinates do not match!
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum HarborBiinvariantVector {
  ;
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction of(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new InfluenceBiinvariantVector( //
        vectorLogManifold, sequence, (TensorMetric & Serializable) (x, y) -> FrobeniusNorm.between(x, y));
  }
}
