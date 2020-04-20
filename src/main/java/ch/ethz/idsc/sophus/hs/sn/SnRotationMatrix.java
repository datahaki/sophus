// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** formula taken from Ethan Eade:
 * Rotation Between Two Vectors in R^3 */
public enum SnRotationMatrix {
  ;
  /** function establishes orthogonal rotation matrix that rotates vector a onto b:
   * <pre>
   * SnRotationMatrix.of(a, b).dot(a) == b
   * </pre>
   * 
   * <p>Hint: the function does not check the 2-norm of a or b.
   * If the input vectors are of 2-norm <b>unequal<b> to 1 the return value will not be orthogonal.
   * 
   * @param a vector with 2-norm equals to 1
   * @param b vector with 2-norm equals to 1
   * @return orthogonal matrix with determinant +1 (only for valid input a and b) with matrix . a == b */
  public static Tensor of(Tensor a, Tensor b) {
    Tensor w = TensorWedge.of(a, b).multiply(RealScalar.of(-2));
    Scalar c = RealScalar.ONE.add(a.dot(b)).reciprocal();
    return IdentityMatrix.of(a.length()).add(w).add(w.dot(w).multiply(c));
  }
}
