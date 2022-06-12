// code by jph
package ch.alpine.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.HsGenesis;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.Tensor;

/** Examples:
 * <pre>
 * HsCoordinates.of(RnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * HsCoordinates.of(SnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * </pre>
 * 
 * @see HsGenesis */
public record HsCoordinates(HsDesign hsDesign, Genesis genesis) implements BarycentricCoordinate, Serializable {
  /** @param manifold
   * @param genesis
   * @return */
  public static BarycentricCoordinate of(Manifold manifold, Genesis genesis) {
    return new HsCoordinates(new HsDesign(manifold), genesis);
  }

  public HsCoordinates {
    Objects.requireNonNull(hsDesign);
    Objects.requireNonNull(genesis);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return genesis.origin(hsDesign.matrix(sequence, point));
  }
}
