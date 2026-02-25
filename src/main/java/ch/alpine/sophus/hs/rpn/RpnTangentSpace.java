// code by jph
package ch.alpine.sophus.hs.rpn;

import java.io.Serializable;

import ch.alpine.sophus.api.TangentSpace;
import ch.alpine.sophus.hs.s.SnAngle;
import ch.alpine.sophus.hs.s.TSnMemberQ;
import ch.alpine.sophus.math.VectorProjection;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sinc;

/** real projective plane */
/* package */ class RpnTangentSpace implements TangentSpace, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final Tensor p;
  private final SnAngle snAngle;
  private final TensorUnaryOperator projection;
  private final TSnMemberQ tSnMemberQ;

  /** @param p on S^n
   * @throws Exception if x is not a vector of Euclidean norm 1 */
  public RpnTangentSpace(Tensor p) {
    this.p = p;
    snAngle = new SnAngle(p);
    tSnMemberQ = new TSnMemberQ(p);
    projection = VectorProjection.on(p);
    if (p.length() < 2)
      throw new Throw(p);
  }

  @Override
  public Tensor basePoint() {
    return p;
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tSnMemberQ.require(v);
    Scalar vn = Vector2Norm.of(v);
    Tensor y = p.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
    y = Vector2Norm.NORMALIZE.apply(y);
    Scalar d_xyp = snAngle.apply(y);
    Scalar d_xyn = snAngle.apply(y.negate());
    return Scalars.lessEquals(d_xyp, d_xyn) //
        ? y
        : y.negate();
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    Scalar d_xyp = snAngle.apply(y);
    Scalar d_xyn = snAngle.apply(y.negate());
    if (Scalars.lessEquals(d_xyp, d_xyn))
      return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(d_xyp);
    y = y.negate();
    return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(d_xyn);
  }

  @Override
  public TSnMemberQ isTangentQ() {
    return tSnMemberQ;
  }
}
