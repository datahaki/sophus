// code by jph
package ch.alpine.sophus.hs.h;

import java.io.Serializable;

import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.chq.ZeroDefectArrayQ;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.tri.ArcCosh;
import ch.alpine.tensor.sca.tri.Cosh;
import ch.alpine.tensor.sca.tri.Sinhc;

/** hyperboloid model
 * 
 * Reference:
 * "Barycentric Subspace Analysis on Manifolds" by Xavier Pennec, 2016
 * 
 * @see HManifold */
public class HTangentSpace implements TangentSpace, Serializable {
  private final HWeierstrassCoordinate pw;
  /** p with extra coordinate */
  private final Tensor p_h;

  HTangentSpace(Tensor p) {
    this.pw = new HWeierstrassCoordinate(p);
    p_h = this.pw.toPoint();
  }

  @Override // from TangentSpace
  public Tensor basePoint() {
    return pw.p();
  }

  /** @param y
   * @return result guaranteed to be greater equals 1 */
  private Scalar _cosh_d(Tensor y) {
    Scalar cosh_d = LBilinearForm.INSTANCE.formEval(p_h, y).negate();
    if (Scalars.lessEquals(RealScalar.ONE, cosh_d))
      return cosh_d;
    Chop._08.requireClose(cosh_d, RealScalar.ONE);
    return RealScalar.ONE;
  }

  @Override // from Exponential
  public Tensor exp(Tensor vx) {
    Scalar vn = pw.toNorm(vx);
    Tensor v = pw.toTangent(vx);
    Tensor exp = p_h.multiply(Cosh.FUNCTION.apply(vn)).add(v.multiply(Sinhc.FUNCTION.apply(vn)));
    return Drop.tail(exp, 1);
  }

  @Override // from Exponential
  public Tensor log(Tensor yx) {
    Tensor y = new HWeierstrassCoordinate(yx).toPoint();
    // embedding of TxH^n in R^(n+1) is not tight
    Scalar cosh_d = _cosh_d(y);
    Scalar angle = ArcCosh.FUNCTION.apply(cosh_d);
    Tensor log = y.subtract(p_h.multiply(cosh_d)).divide(Sinhc.FUNCTION.apply(angle));
    return Drop.tail(log, 1);
  }

  @Override // from Exponential
  public ZeroDefectArrayQ isTangentQ() {
    return VectorQ.ofLength(pw.p().length());
  }
}
