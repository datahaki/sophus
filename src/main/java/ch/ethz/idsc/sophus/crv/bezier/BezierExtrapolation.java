// code by jph
package ch.ethz.idsc.sophus.crv.bezier;

import java.util.Objects;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.itp.BinaryAverage;

/** extrapolation by evaluating the Bezier curve defined by n number of
 * control points at parameter value n / (n - 1) */
public class BezierExtrapolation implements TensorUnaryOperator {
  /** @param binaryAverage
   * @return */
  public static TensorUnaryOperator of(BinaryAverage binaryAverage) {
    return new BezierExtrapolation(Objects.requireNonNull(binaryAverage));
  }

  /***************************************************/
  private final BinaryAverage binaryAverage;

  private BezierExtrapolation(BinaryAverage binaryAverage) {
    this.binaryAverage = binaryAverage;
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    int n = tensor.length();
    return BezierFunction.of(binaryAverage, tensor).apply(RationalScalar.of(n, n - 1));
  }
}
