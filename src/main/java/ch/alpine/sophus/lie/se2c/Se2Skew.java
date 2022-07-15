// code by ob, jph
package ch.alpine.sophus.lie.se2c;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.r2.RotationMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.re.LinearSolve;
import ch.alpine.tensor.sca.tri.Tan;

/** solve for biinvariant mean
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.35, Section 4.5
 * 
 * (1 - Cos[t]) / Sin[t] == Tan[t/2] */
/* package */ class Se2Skew {
  private static final Scalar HALF = RealScalar.of(0.5);

  /** @param xya element in SE(2)
   * @param weight
   * @return */
  public static Se2Skew of(Tensor xya, Scalar weight) {
    Tensor xy = xya.extract(0, 2);
    Scalar angle = xya.Get(2).negate();
    Tensor logflow = logflow(angle);
    return new Se2Skew(logflow.multiply(weight), logflow.dot(RotationMatrix.of(angle).dot(xy.multiply(weight))));
  }

  /** Function returns 2x2 matrix that transforms (x, y) part of group element in log
   * 
   * The determinant of the matrix is of the form
   * (t^2 Cos[t])/(2 - 2 Cos[t])
   * which evaluates to zero for t == pi/2 + z pi where z is any integer
   * 
   * @param be angle
   * @return matrix of dimensions 2 x 2 */
  public static Tensor logflow(Scalar be) {
    Scalar be2 = be.multiply(HALF);
    Scalar tan = Tan.FUNCTION.apply(be2);
    if (Scalars.isZero(tan))
      return IdentityMatrix.of(2);
    Scalar m11 = be2.divide(tan);
    return Tensors.of( //
        Tensors.of(m11, be2), //
        Tensors.of(be2.negate(), m11));
  }

  // ---
  /** matrix with dimensions 2 x 2 */
  private final Tensor lhs;
  /** vector of length 2 */
  private final Tensor rhs;

  private Se2Skew(Tensor lhs, Tensor rhs) {
    this.lhs = lhs;
    this.rhs = rhs;
  }

  public Se2Skew add(Se2Skew se2Skew) {
    return new Se2Skew( //
        lhs.add(se2Skew.lhs), //
        rhs.add(se2Skew.rhs));
  }

  /** @return vector of length 2 */
  public Tensor solve() {
    return LinearSolve.of(lhs, rhs);
  }

  public Tensor rhs() {
    return rhs.copy();
  }
}
