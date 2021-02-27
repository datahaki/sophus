// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.sn.SnAngle;
import ch.ethz.idsc.sophus.hs.sn.TSnMemberQ;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.red.Projection;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Sinc;

/** real projective plane */
public class RpnExponential implements Exponential, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final Tensor x;
  private final SnAngle snAngle;
  private final TensorUnaryOperator projection;
  private final TSnMemberQ tSnMemberQ;

  /** @param x on S^n
   * @throws Exception if x is not a vector of Euclidean norm 1 */
  public RpnExponential(Tensor x) {
    this.x = x;
    snAngle = new SnAngle(x);
    tSnMemberQ = new TSnMemberQ(x);
    projection = Projection.on(x);
    if (x.length() < 2)
      throw TensorRuntimeException.of(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    tSnMemberQ.require(v);
    Scalar vn = Vector2Norm.of(v);
    Tensor y = x.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
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

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return log(y);
  }
}
