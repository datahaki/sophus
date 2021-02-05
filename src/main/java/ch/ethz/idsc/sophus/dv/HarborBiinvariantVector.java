// code by jph
package ch.ethz.idsc.sophus.dv;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.BiinvariantVectorFunction;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.TensorMetric;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Frobenius;

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
        vectorLogManifold, sequence, (TensorMetric & Serializable) (x, y) -> Frobenius.between(x, y));
  }
}
