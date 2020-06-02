// code by jph
package ch.ethz.idsc.sophus.hs.rpn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.hs.sn.SnMetric;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.opt.Projection;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Sinc;

/** real projective plane */
public class RpnExponential implements Exponential, TangentSpace, Serializable {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final Tensor x;
  private final TensorUnaryOperator projection;

  /** @param x on S^n
   * @throws Exception if x is not a vector of Euclidean norm 1 */
  public RpnExponential(Tensor x) {
    this.x = StaticHelper.requirePoint(x);
    projection = Projection.on(x);
    if (x.length() < 2)
      throw TensorRuntimeException.of(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    StaticHelper.requireTangent(x, v);
    Scalar vn = Norm._2.ofVector(v);
    Tensor y = x.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
    y = NORMALIZE.apply(y);
    Scalar d_xyp = SnMetric.INSTANCE.distance(x, y);
    Scalar d_xyn = SnMetric.INSTANCE.distance(x, y.negate());
    return Scalars.lessEquals(d_xyp, d_xyn) //
        ? y
        : y.negate();
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    StaticHelper.requirePoint(y);
    Scalar d_xyp = SnMetric.INSTANCE.distance(x, y);
    Scalar d_xyn = SnMetric.INSTANCE.distance(x, y.negate());
    if (Scalars.lessEquals(d_xyp, d_xyn))
      return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(d_xyp);
    y = y.negate();
    return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(d_xyn);
  }

  @Override // from FlattenLog
  public Tensor vectorLog(Tensor y) {
    return log(y);
  }
}
