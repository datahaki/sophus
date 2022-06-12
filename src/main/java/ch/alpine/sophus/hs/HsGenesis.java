// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public enum HsGenesis {
  ;
  /** @param manifold
   * @param genesis
   * @param sequence non-null
   * @return */
  public static TensorUnaryOperator wrap(Manifold manifold, Genesis genesis, Tensor sequence) {
    BarycentricCoordinate barycentricCoordinate = HsCoordinates.of(manifold, genesis);
    Objects.requireNonNull(sequence);
    return point -> barycentricCoordinate.weights(sequence, point);
  }
}
