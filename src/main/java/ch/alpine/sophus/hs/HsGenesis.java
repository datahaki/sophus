// code by jph
package ch.alpine.sophus.hs;

import java.util.Objects;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.gbc.BarycentricCoordinate;
import ch.alpine.sophus.gbc.HsCoordinates;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public enum HsGenesis {
  ;
  /** @param vectorLogManifold
   * @param genesis
   * @param sequence
   * @return */
  public static TensorUnaryOperator wrap(VectorLogManifold vectorLogManifold, Genesis genesis, Tensor sequence) {
    Objects.requireNonNull(sequence);
    BarycentricCoordinate barycentricCoordinate = HsCoordinates.wrap(vectorLogManifold, genesis);
    return point -> barycentricCoordinate.weights(sequence, point);
  }
}
