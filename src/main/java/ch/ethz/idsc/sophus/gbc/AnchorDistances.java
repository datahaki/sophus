// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsInfluence;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.BiinvariantVector;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;

/** Hint: DO NOT USE AnchorDistances EXCEPT IN AnchorCoordinate !!! */
/* package */ class AnchorDistances implements WeightingInterface, Serializable {
  private final VectorLogManifold vectorLogManifold;

  /** @param vectorLogManifold */
  public AnchorDistances(VectorLogManifold vectorLogManifold) {
    this.vectorLogManifold = vectorLogManifold;
  }

  public BiinvariantVector biinvariantVector(Tensor sequence, Tensor point) {
    HsInfluence hsInfluence = new HsInfluence(vectorLogManifold.logAt(point), sequence);
    return new BiinvariantVector(hsInfluence.matrix(), hsInfluence.leverages_sqrt());
  }

  @Override // from WeightingInterface
  public Tensor weights(Tensor sequence, Tensor point) {
    return biinvariantVector(sequence, point).distances();
  }
}
