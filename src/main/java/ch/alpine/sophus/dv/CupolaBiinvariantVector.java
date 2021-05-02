// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.BiinvariantVectorFunction;
import ch.alpine.sophus.hs.VectorLogManifold;
import ch.alpine.sophus.hs.gr.GrMetric;
import ch.alpine.tensor.Tensor;

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
