// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
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
    Biinvariant biinvariant = Biinvariants.METRIC.of(RnGroup.INSTANCE);
    return new BarycentricMetricInterpolation( //
        biinvariant.coordinate(variogram, knots.map(Tensors::of))::sunder);
  }

  public static ScalarTensorFunction la(Tensor knots, ScalarUnaryOperator variogram) {
    Biinvariant biinvariant = Biinvariants.METRIC.of(RnGroup.INSTANCE);
    return new BarycentricMetricInterpolation( //
        biinvariant.lagrainate(variogram, knots.map(Tensors::of))::sunder);
  }

  // ---
  private final TensorUnaryOperator tensorUnaryOperator;

  private BarycentricMetricInterpolation(TensorUnaryOperator tensorUnaryOperator) {
    this.tensorUnaryOperator = tensorUnaryOperator;
  }

  @Override
  public Tensor apply(Scalar scalar) {
    // TODO SOPHUS WHUT !?
    return tensorUnaryOperator.apply(Tensors.of(scalar));
  }
}
