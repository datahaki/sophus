// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

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
