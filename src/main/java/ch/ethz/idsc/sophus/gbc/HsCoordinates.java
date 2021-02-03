// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Examples:
 * <pre>
 * HsCoordinates.wrap(RnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * HsCoordinates.wrap(SnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * </pre> */
public class HsCoordinates implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param genesis
   * @return */
  public static BarycentricCoordinate wrap(VectorLogManifold vectorLogManifold, Genesis genesis) {
    return new HsCoordinates(vectorLogManifold, genesis);
  }

  /** @param vectorLogManifold
   * @param genesis
   * @param sequence
   * @return */
  public static TensorUnaryOperator wrap(VectorLogManifold vectorLogManifold, Genesis genesis, Tensor sequence) {
    BarycentricCoordinate barycentricCoordinate = wrap(vectorLogManifold, genesis);
    Objects.requireNonNull(sequence);
    return point -> barycentricCoordinate.weights(sequence, point);
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
