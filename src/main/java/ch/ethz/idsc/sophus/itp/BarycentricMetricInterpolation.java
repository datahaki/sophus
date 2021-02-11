// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.hs.Biinvariants;
import ch.ethz.idsc.sophus.lie.rn.RnManifold;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.api.ScalarTensorFunction;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** for comparison with {@link BarycentricRationalInterpolation} */
public class BarycentricMetricInterpolation implements ScalarTensorFunction {
  /** @param knots
   * @param variogram
   * @return */
  public static ScalarTensorFunction of(Tensor knots, ScalarUnaryOperator variogram) {
    return new BarycentricMetricInterpolation( //
        Biinvariants.METRIC.coordinate(RnManifold.INSTANCE, variogram, knots.map(Tensors::of)));
  }

  public static ScalarTensorFunction la(Tensor knots, ScalarUnaryOperator variogram) {
    return new BarycentricMetricInterpolation( //
        Biinvariants.METRIC.lagrainate(RnManifold.INSTANCE, variogram, knots.map(Tensors::of)));
  }

  /***************************************************/
  private final TensorUnaryOperator tensorUnaryOperator;

  private BarycentricMetricInterpolation(TensorUnaryOperator tensorUnaryOperator) {
    this.tensorUnaryOperator = tensorUnaryOperator;
  }

  @Override
  public Tensor apply(Scalar scalar) {
    return tensorUnaryOperator.apply(Tensors.of(scalar));
  }
}
