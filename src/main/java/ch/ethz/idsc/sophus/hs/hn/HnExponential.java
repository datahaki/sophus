// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.sca.Sinhc;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Cosh;

/** hyperboloid model
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
public class HnExponential implements Exponential, TangentSpace, Serializable {
  private final Tensor x;
  private final THnMemberQ tHnMemberQ;
  private final HnAngle hnAngle;

  public HnExponential(Tensor x) {
    this.x = x;
    hnAngle = new HnAngle(x);
    tHnMemberQ = new THnMemberQ(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tHnMemberQ.require(v);
    Scalar vn = HnNorm.INSTANCE.norm(v);
    Tensor exp = x.multiply(Cosh.FUNCTION.apply(vn)).add(v.multiply(Sinhc.FUNCTION.apply(vn)));
    return HnProjection.INSTANCE.apply(exp);
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return hnAngle.log(y);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return log(y); // TODO think about using hnAngle.vectorLog !?
  }
}
