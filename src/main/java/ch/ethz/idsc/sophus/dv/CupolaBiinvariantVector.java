// code by jph
package ch.ethz.idsc.sophus.dv;

import ch.ethz.idsc.sophus.hs.BiinvariantVectorFunction;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.hs.gr.GrMetric;
import ch.ethz.idsc.tensor.Tensor;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum CupolaBiinvariantVector {
  ;
  /** @param vectorLogManifold
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction of(VectorLogManifold vectorLogManifold, Tensor sequence) {
    return new InfluenceBiinvariantVector(vectorLogManifold, sequence, GrMetric.INSTANCE);
  }
}
