// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.dv.Biinvariants;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.api.ScalarUnaryOperator;

/** for comparison with {@link BarycentricRationalInterpolation} */
public class BarycentricMetricInterpolation implements ScalarTensorFunction {
  /** @param knots
   * @param variogram
   * @return */
  public static ScalarTensorFunction of(Tensor knots, ScalarUnaryOperator variogram) {
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    return new BarycentricMetricInterpolation(biinvariant.coordinate(variogram, knots.map(Tensors::of)));
  }

  public static ScalarTensorFunction la(Tensor knots, ScalarUnaryOperator variogram) {
    Biinvariant biinvariant = Biinvariants.METRIC.ofSafe(RnGroup.INSTANCE);
    return new BarycentricMetricInterpolation(biinvariant.lagrainate(variogram, knots.map(Tensors::of)));
  }

  // ---
  private final Sedarim sedarim;

  private BarycentricMetricInterpolation(Sedarim sedarim) {
    this.sedarim = sedarim;
  }

  @Override
  public Tensor apply(Scalar scalar) {
    // QUEST SOPHUS review entire class and document
    return sedarim.sunder(Tensors.of(scalar));
  }
}
