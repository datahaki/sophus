// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.FlattenLog;
import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.alg.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.opt.Projection;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.red.VectorAngle;
import ch.ethz.idsc.tensor.sca.Cos;
import ch.ethz.idsc.tensor.sca.Sinc;

/** Exponential map of S^n manifold at given point
 * 
 * The "orthogonal" property is established by using the dot product in R^(n+1)
 * 
 * function exp throws an exception if input x is not orthogonal to given point
 * function log returns vector orthogonal to x when using the dot product in R^(n+1)
 * 
 * implementation is based on the function "strans" taken from
 * "Freeform Curves on Spheres of Arbitrary Dimension"
 * by Scott Schaefer and Ron Goldman, 2005, page 5 */
public class SnExponential implements Exponential, FlattenLog, Serializable {
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Norm._2);
  // ---
  private final Tensor x;
  private final TensorUnaryOperator projection;

  /** @param x on S^n
   * @throws Exception if p is not a vector of Euclidean norm 1 */
  public SnExponential(Tensor x) {
    this.x = StaticHelper.requirePoint(x);
    projection = Projection.on(x);
    if (x.length() < 2)
      throw TensorRuntimeException.of(x);
  }

  @Override // from LieExponential
  public Tensor exp(Tensor v) {
    StaticHelper.requireTangent(x, v);
    Scalar vn = Norm._2.ofVector(v);
    Tensor y = x.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
    return NORMALIZE.apply(y);
  }

  @Override // from LieExponential
  public Tensor log(Tensor y) {
    StaticHelper.requirePoint(y);
    return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(VectorAngle.of(x, y).get());
  }

  @Override
  public Tensor flattenLog(Tensor y) {
    return log(y);
  }
}
