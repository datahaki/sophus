// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.rot.Cross;
import ch.alpine.tensor.mat.IdentityMatrix;

/** formula taken from Ethan Eade:
 * Rotation Between Two Vectors in R^3
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/RotationMatrix.html">RotationMatrix</a> */
/* package */ enum RotationMatrix3D {
  ;
  private static final Tensor ID3 = IdentityMatrix.of(3);

  /** function establishes 3 x 3 rotation matrix that rotates vector a onto b:
   * <pre>
   * RotationMatrix3D.of(a, b).dot(a) == b
   * </pre>
   * 
   * <p>Hint: the function does not check the 2-norm of a or b.
   * If the input vectors are of 2-norm <b>unequal<b> to 1 the return value will not be orthogonal.
   * 
   * @param a vector with 3 entries and 2-norm equals to 1
   * @param b vector with 3 entries and 2-norm equals to 1
   * @return 3 x 3 orthogonal matrix with determinant +1 (only for valid input a and b) */
  public static Tensor of(Tensor a, Tensor b) {
    Tensor w = Cross.of(a, b);
    Tensor wx = Cross.skew3(w);
    Scalar ab = (Scalar) a.dot(b);
    Scalar c = ab.add(RealScalar.ONE).reciprocal();
    return ID3.add(wx).add(wx.dot(wx).multiply(c));
  }
}
