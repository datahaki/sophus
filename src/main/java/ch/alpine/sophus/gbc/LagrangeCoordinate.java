// code by jph
package ch.alpine.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;

/** attempts to produce positive weights for levers with zero in convex hull
 * 
 * Technique of using Lagrange multipliers inspired by the following reference:
 * "Polygon Laplacian Made Simple"
 * by Astrid Bunge, Philipp Herholz, Misha Kazhdan, Mario Botsch, 2020 */
public class LagrangeCoordinate implements Genesis, Serializable {
  /** @param genesis for instance InverseDistanceWeighting.of(InversePowerVariogram.of(2))
   * @return */
  public static Genesis of(Genesis genesis) {
    return new LagrangeCoordinate(Objects.requireNonNull(genesis));
  }

  /***************************************************/
  private final Genesis genesis;

  private LagrangeCoordinate(Genesis genesis) {
    this.genesis = genesis;
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return LagrangeCoordinates.of(levers, genesis.origin(levers));
  }
}
