// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.dv.BarycentricCoordinate;
import ch.alpine.sophus.dv.HsCoordinates;
import ch.alpine.tensor.Tensor;

public enum HsGenesis {
  ;
  /** @param manifold
   * @param genesis
   * @param sequence non-null
   * @return */
  public static Sedarim wrap(HsDesign hsDesign, Genesis genesis, Tensor sequence) {
    BarycentricCoordinate barycentricCoordinate = new HsCoordinates(hsDesign, genesis);
    Objects.requireNonNull(sequence);
    return point -> barycentricCoordinate.weights(sequence, point);
  }
}
