// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.itp.BinaryAverage;

public class SplitParametricCurve implements Geodesic, Serializable {
  /** @param binaryAverage
   * @return */
  public static Geodesic of(BinaryAverage binaryAverage) {
    return new SplitParametricCurve(Objects.requireNonNull(binaryAverage));
  }

  /***************************************************/
  private final BinaryAverage binaryAverage;

  private SplitParametricCurve(BinaryAverage binaryAverage) {
    this.binaryAverage = binaryAverage;
  }

  @Override // from ParametricCurve
  public ScalarTensorFunction curve(Tensor p, Tensor q) {
    return scalar -> binaryAverage.split(p, q, scalar);
  }

  @Override // from SplitInterface
  public Tensor split(Tensor p, Tensor q, Scalar scalar) {
    return binaryAverage.split(p, q, scalar);
  }
}
