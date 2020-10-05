// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.hs.TangentSpace;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.opt.Projection;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Sinc;

/** Exponential map of S^n manifold at given point
 * 
 * The "orthogonal" property is established by using the dot product in R^(n+1)
 * 
 * function exp throws an exception if input x is not orthogonal to given point
 * function log returns vector orthogonal to x when using the dot product in R^(n+1)
 * 
 * implementation of exp is based on the function "strans" taken from
 * "Freeform Curves on Spheres of Arbitrary Dimension"
 * by Scott Schaefer and Ron Goldman, 2005, page 5
 * 
 * implementation of log is based on
 * "Barycentric Subspace Analysis on Manifolds"
 * by Xavier Pennec, 2016, p. 8 */
public class SnExponential implements Exponential, TangentSpace, Serializable {
  private static final long serialVersionUID = -3715922243477203953L;
  private static final HsMemberQ HS_MEMBER_Q = SnMemberQ.of(Chop._06);
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final Tensor x;
  private final TensorUnaryOperator projection;

  /** @param x on S^n
   * @throws Exception if x is not a vector of Euclidean norm 1 */
  public SnExponential(Tensor x) {
    this.x = HS_MEMBER_Q.requirePoint(x);
    projection = Projection.on(x);
    if (x.length() < 2)
      throw TensorRuntimeException.of(x);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    HS_MEMBER_Q.requireTangent(x, v);
    Scalar vn = Hypot.ofVector(v);
    Tensor y = x.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
    return NORMALIZE.apply(y);
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    HS_MEMBER_Q.requirePoint(y);
    Scalar d_xy = SnMetric.INSTANCE.distance(x, y);
    return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(d_xy);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return log(y);
  }
}
