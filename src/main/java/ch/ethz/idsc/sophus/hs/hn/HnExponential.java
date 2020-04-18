// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.sca.Sinhc;
import ch.ethz.idsc.sophus.math.sca.SinhcInverse;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ArcCosh;
import ch.ethz.idsc.tensor.sca.Cosh;

/** hyperboloid model
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
public class HnExponential implements Exponential, FlattenLog, Serializable {
  private final Tensor x;

  public HnExponential(Tensor x) {
    this.x = StaticHelper.requirePoint(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    StaticHelper.requireTangent(x, v);
    Scalar vn = HnNorm.INSTANCE.norm(v);
    Tensor exp = x.multiply(Cosh.FUNCTION.apply(vn)).add(v.multiply(Sinhc.FUNCTION.apply(vn)));
    return HnProjection.INSTANCE.apply(exp);
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    Scalar cosh_d = HnMetric.cosh_d(x, y);
    Scalar theta = ArcCosh.FUNCTION.apply(cosh_d);
    return y.subtract(x.multiply(cosh_d)).multiply(SinhcInverse.FUNCTION.apply(theta));
  }

  @Override // from FlattenLog
  public Tensor flattenLog(Tensor y) {
    return log(y);
  }
}
