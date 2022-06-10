// code by jph
package ch.alpine.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.hs.Genesis;
import ch.alpine.tensor.Tensor;

/** attempts to produce positive weights for levers with zero in convex hull
 * 
 * Technique of using Lagrange multipliers inspired by the following reference:
 * "Polygon Laplacian Made Simple"
 * by Astrid Bunge, Philipp Herholz, Misha Kazhdan, Mario Botsch, 2020
 * 
 * @param genesis for instance InverseDistanceWeighting.of(InversePowerVariogram.of(2)) */
public record LagrangeCoordinate(Genesis genesis) implements Genesis, Serializable {
  public LagrangeCoordinate {
    Objects.requireNonNull(genesis);
  }

  @Override // from Genesis
  public Tensor origin(Tensor levers) {
    return LagrangeCoordinates.of(levers, genesis.origin(levers));
  }
}
