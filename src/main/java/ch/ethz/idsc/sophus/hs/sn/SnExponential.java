// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.nrm.NormalizeUnlessZero;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import ch.ethz.idsc.tensor.red.Projection;
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
public class SnExponential implements Exponential, Serializable {
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(VectorNorm2::of);
  // ---
  private final Tensor x;
  private final SnAngle snAngle;
  private final TensorUnaryOperator projection;
  private final TSnMemberQ tSnMemberQ;

  /** @param x on S^n
   * @throws Exception if x is not a vector of Euclidean norm 1 */
  public SnExponential(Tensor x) {
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
    Scalar vn = VectorNorm2.of(v);
    Tensor y = x.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
    return VectorNorm2.NORMALIZE.apply(y);
  }

  @Override // from Exponential
  public Tensor log(Tensor y) {
    Scalar d_xy = snAngle.apply(y); // throws an Exception if y not member of Sn
    return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(d_xy);
  }

  @Override // from TangentSpace
  public Tensor vectorLog(Tensor y) {
    return log(y);
  }
}
