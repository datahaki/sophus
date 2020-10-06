// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.util.Objects;

import ch.ethz.idsc.sophus.lie.r2.Polygons;
import ch.ethz.idsc.tensor.DoubleScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.ConstantArray;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class InsidePolygonCoordinate implements TensorUnaryOperator {
  /** @param tensorUnaryOperator
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator) {
    return new InsidePolygonCoordinate(tensorUnaryOperator);
  }

  /***************************************************/
  private final TensorUnaryOperator tensorUnaryOperator;

  /** @param vectorLogManifold
   * @param tensorUnaryOperator that evaluates polygon coordinates at zero (0, 0) */
  private InsidePolygonCoordinate(TensorUnaryOperator tensorUnaryOperator) {
    this.tensorUnaryOperator = Objects.requireNonNull(tensorUnaryOperator);
  }

  @Override // from BarycentricCoordinate
  public Tensor apply(Tensor levers) {
    return Polygons.isInside(levers) //
        ? tensorUnaryOperator.apply(levers)
        : ConstantArray.of(DoubleScalar.INDETERMINATE, levers.length());
  }
}
