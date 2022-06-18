// code by jph
package ch.alpine.sophus.dv;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.gr.GrManifold;
import ch.alpine.tensor.Tensor;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum CupolaBiinvariantVector {
  ;
  /** @param hsDesign
   * @param sequence
   * @return */
  public static BiinvariantVectorFunction of(HsDesign hsDesign, Tensor sequence) {
    return new InfluenceBiinvariantVector(hsDesign, sequence, GrManifold.INSTANCE);
  }
}
