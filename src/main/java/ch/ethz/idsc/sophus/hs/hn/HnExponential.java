// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.sophus.math.sca.Sinhc;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Cosh;

/** hyperboloid model
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016
 * 
 * @see HnGeodesic */
public class HnExponential implements Exponential, Serializable {
  private final Tensor p;
  private final HnAngle hnAngle;
  private final THnMemberQ tHnMemberQ;

  public HnExponential(Tensor p) {
    this.p = p;
    hnAngle = new HnAngle(p);
    tHnMemberQ = new THnMemberQ(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tHnMemberQ.require(v);
    Scalar vn = HnVectorNorm.of(v);
    return p.multiply(Cosh.FUNCTION.apply(vn)).add(v.multiply(Sinhc.FUNCTION.apply(vn)));
  }

  @Override // from Exponential
  public Tensor log(Tensor q) {
    return hnAngle.log(q);
  }

  @Override // from Exponential
  public Tensor flip(Tensor q) {
    return HnManifold.INSTANCE.flip(p, q);
  }

  @Override // from Exponential
  public Tensor midpoint(Tensor q) {
    return HnManifold.INSTANCE.midpoint(p, q);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor q) {
    // embedding of TxH^n in R^(n+1) is not tight
    return log(q);
  }
}
