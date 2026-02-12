// code by jph
package ch.alpine.sophus.hs.s;

import java.io.Serializable;

import ch.alpine.sophus.hs.Exponential;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.nrm.NormalizeUnlessZero;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.red.Projection;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sinc;

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
  private static final TensorUnaryOperator NORMALIZE_UNLESS_ZERO = NormalizeUnlessZero.with(Vector2Norm::of);
  // ---
  private final Tensor p;
  private final SnAngle snAngle;
  private final TensorUnaryOperator projection;
  private final TSnMemberQ tSnMemberQ;

  /** only needed for vectorLog */
  /** @param p on S^n
   * @throws Exception if x is not a vector of Euclidean norm 1 */
  public SnExponential(Tensor p) {
    this.p = p;
    snAngle = new SnAngle(p);
    tSnMemberQ = new TSnMemberQ(p);
    projection = Projection.on(p);
  }

  @Override // from Exponential
  public Tensor exp(Tensor v) {
    Scalar vn = Vector2Norm.of(v);
    return p.multiply(Cos.FUNCTION.apply(vn)).add(v.multiply(Sinc.FUNCTION.apply(vn)));
  }

  /** @throws Exception if y not member of Sn */
  @Override // from Exponential
  public Tensor log(Tensor y) {
    return NORMALIZE_UNLESS_ZERO.apply(y.subtract(projection.apply(y))).multiply(snAngle.apply(y));
  }

  public Tensor endomorphism(Tensor q) {
    return SnRotationMatrix.of(p, q);
  }

  @Override
  public MemberQ isTangentQ() {
    return tSnMemberQ;
  }
}
