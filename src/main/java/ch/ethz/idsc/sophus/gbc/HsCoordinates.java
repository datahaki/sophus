// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.Tensor;

/** Examples:
 * <pre>
 * HsCoordinates.wrap(RnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * HsCoordinates.wrap(SnManifold.INSTANCE, ThreePointCoordinate.of(Barycenter.MEAN_VALUE))
 * </pre> */
public class HsCoordinates implements BarycentricCoordinate, Serializable {
  /** @param vectorLogManifold
   * @param tensorUnaryOperator
   * @return */
  public static BarycentricCoordinate wrap( //
      VectorLogManifold vectorLogManifold, ZeroCoordinate tensorUnaryOperator) {
    return new HsCoordinates(vectorLogManifold, tensorUnaryOperator);
  }

  /***************************************************/
  private final HsDesign hsDesign;
  private final ZeroCoordinate tensorUnaryOperator;

  private HsCoordinates(VectorLogManifold vectorLogManifold, ZeroCoordinate tensorUnaryOperator) {
    hsDesign = new HsDesign(vectorLogManifold);
    this.tensorUnaryOperator = Objects.requireNonNull(tensorUnaryOperator);
  }

  @Override // from BarycentricCoordinate
  public Tensor weights(Tensor sequence, Tensor point) {
    return tensorUnaryOperator.fromLevers(hsDesign.matrix(sequence, point));
  }
}
