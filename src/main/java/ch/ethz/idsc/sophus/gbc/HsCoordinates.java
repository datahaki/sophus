// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.HsGenesis;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;

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
  public static BarycentricCoordinate wrap(VectorLogManifold vectorLogManifold, Genesis genesis) {
    return new HsCoordinates(vectorLogManifold, genesis);
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final Genesis genesis;

  private HsCoordinates(VectorLogManifold vectorLogManifold, Genesis genesis) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.genesis = Objects.requireNonNull(genesis);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return genesis.origin(hsDesign.matrix(sequence, point));
  }
}
