// code by jph
package ch.alpine.sophus.dv;

import java.io.Serializable;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.sophus.hs.HsDesign;
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
  /** @param manifold
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction of(HsDesign hsDesign, Tensor sequence) {
    return new InfluenceBiinvariantVector( //
        hsDesign, sequence, (TensorMetric & Serializable) (x, y) -> FrobeniusNorm.between(x, y));
  }
}
