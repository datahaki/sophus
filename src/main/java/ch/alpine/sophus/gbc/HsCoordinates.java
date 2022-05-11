// code by jph
package ch.alpine.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.HsGenesis;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;

/** Examples:
 * <pre>
 * HsCoordinates.wrap(RnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * HsCoordinates.wrap(SnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * </pre>
 * 
 * @see HsGenesis */
public class HsCoordinates implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param genesis
   * @return */
  public static BarycentricCoordinate wrap(Manifold vectorLogManifold, Genesis genesis) {
    return new HsCoordinates(vectorLogManifold, genesis);
  }

  // ---
  private final HsDesign hsDesign;
  private final Genesis genesis;

  private HsCoordinates(Manifold vectorLogManifold, Genesis genesis) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.genesis = Objects.requireNonNull(genesis);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return genesis.origin(hsDesign.matrix(sequence, point));
  }
}
