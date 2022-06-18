// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.BiinvariantVectorFunction;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.gr.GrMetric;
import ch.alpine.tensor.Tensor;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum CupolaBiinvariantVector {
  ;
  /** @param manifold
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction of(Manifold manifold, Tensor sequence) {
    return new InfluenceBiinvariantVector(new HsDesign(manifold), sequence, GrMetric.INSTANCE);
  }
}
