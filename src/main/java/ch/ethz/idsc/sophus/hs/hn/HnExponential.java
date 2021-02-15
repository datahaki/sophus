// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** hyperboloid model
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016 */
public class HnExponential implements Exponential, Serializable {
  private final Tensor x;
  private final HnAngle hnAngle;
  private final THnMemberQ tHnMemberQ;

  public HnExponential(Tensor x) {
    this.x = x;
    hnAngle = new HnAngle(x);
    tHnMemberQ = new THnMemberQ(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tHnMemberQ.require(v);
    Scalar n2 = HnNorm.squared(v);
    Tensor res = HnSeries.of(n2);
    Scalar cosh = res.Get(0);
    Scalar sinhc = res.Get(1);
    Tensor exp = x.multiply(cosh).add(v.multiply(sinhc));
    // the error here is really bad: ratio approx. 0.99 - 1.01
    // System.out.println(HnMemberQ.ratio(exp));
    exp = HnProjection.INSTANCE.apply(exp);
    // HnMemberQ.INSTANCE.require(exp);
    return exp; //
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    return hnAngle.log(y);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    // embedding of TxH^n in R^(n+1) is not tight (consistent with S^n)
    return log(y);
  }
}
