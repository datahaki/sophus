// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.BiinvariantVector;
import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;

/** Hint: DO NOT USE AnchorDistances EXCEPT IN AnchorCoordinate !!! */
/* package */ class AnchorDistances implements WeightingInterface, Serializable {
  private final HsDesign hsDesign;

  /** @param vectorLogManifold */
  public AnchorDistances(VectorLogManifold vectorLogManifold) {
    hsDesign = new HsDesign(vectorLogManifold);
  }

  public BiinvariantVector biinvariantVector(Tensor sequence, Tensor point) {
    InfluenceMatrix influenceMatrix = InfluenceMatrix.of(hsDesign.matrix(sequence, point));
    return new BiinvariantVector(influenceMatrix, influenceMatrix.leverages_sqrt());
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return biinvariantVector(sequence, point).distances();
  }
}
