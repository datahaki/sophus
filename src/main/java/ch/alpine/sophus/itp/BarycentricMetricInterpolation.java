// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.dv.MetricBiinvariant;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.api.TensorUnaryOperator;

/** for comparison with {@link BarycentricRationalInterpolation} */
// TODO SOPHUS strange functionality because very specific!
public class BarycentricMetricInterpolation implements ScalarTensorFunction {
  /** @param knots
   * @param variogram
   * @return */
  public static ScalarTensorFunction of(Tensor knots, ScalarUnaryOperator variogram) {
    MetricBiinvariant metricBiinvariant = new MetricBiinvariant(RnGroup.INSTANCE);
    return new BarycentricMetricInterpolation( //
        metricBiinvariant.coordinate(variogram, knots.map(Tensors::of)));
  }

  public static ScalarTensorFunction la(Tensor knots, ScalarUnaryOperator variogram) {
    MetricBiinvariant metricBiinvariant = new MetricBiinvariant(RnGroup.INSTANCE);
    return new BarycentricMetricInterpolation( //
        metricBiinvariant.lagrainate(variogram, knots.map(Tensors::of)));
  }

  // ---
  private final TensorUnaryOperator tensorUnaryOperator;

  private BarycentricMetricInterpolation(TensorUnaryOperator tensorUnaryOperator) {
    this.tensorUnaryOperator = tensorUnaryOperator;
  }

  @Override
  public Tensor apply(Scalar scalar) {
    return tensorUnaryOperator.apply(Tensors.of(scalar));
  }
}
