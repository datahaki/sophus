// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.lie.r2.Polygons;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;

public class InsidePolygonCoordinate implements ZeroCoordinate, Serializable {
  /** @param tensorUnaryOperator
   * @return */
  public static ZeroCoordinate of(ZeroCoordinate tensorUnaryOperator) {
    return new InsidePolygonCoordinate(tensorUnaryOperator);
  }

  /***************************************************/
  private final ZeroCoordinate tensorUnaryOperator;

  /** @param vectorLogManifold
   * @param tensorUnaryOperator that evaluates polygon coordinates at zero (0, 0) */
  private InsidePolygonCoordinate(ZeroCoordinate tensorUnaryOperator) {
    this.tensorUnaryOperator = Objects.requireNonNull(tensorUnaryOperator);
  }

  @Override // from BarycentricCoordinate
  public Tensor fromLevers(Tensor levers) {
    return Polygons.isInside(levers) //
        ? tensorUnaryOperator.fromLevers(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}
