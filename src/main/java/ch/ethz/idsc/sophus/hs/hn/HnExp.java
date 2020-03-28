// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.lie.LieExponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Cosh;
import ch.ethz.idsc.tensor.sca.Sinh;

/** Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
public class HnExp implements LieExponential, FlattenLog, Serializable {
  private final Tensor x;

  public HnExp(Tensor x) {
    this.x = StaticHelper.requirePoint(x);
  }

  @Override // from LieExponential
  public Tensor exp(Tensor v) {
    StaticHelper.requireTangent(x, v);
    Scalar vn = HnNorm.INSTANCE.norm(v);
    return x.multiply(Cosh.FUNCTION.apply(vn)).add(v.multiply(Sinh.FUNCTION.apply(vn).divide(vn)));
  }

  @Override // from LieExponential
  public Tensor log(Tensor y) {
    StaticHelper.requirePoint(y);
    Scalar theta = HnMetric.INSTANCE.distance(x, y);
    // FIXME use series of theta/sinh(theta) if theta is small
    return y.subtract(x.multiply(theta)).multiply(theta).divide(Sinh.FUNCTION.apply(theta));
  }

  @Override // from FlattenLog
  public Tensor flattenLog(Tensor y) {
    return log(y);
  }
}
