// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Norm;

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
    Tolerance.CHOP.requireClose(Norm._2.ofVector(a), RealScalar.ONE);
    Tolerance.CHOP.requireClose(Norm._2.ofVector(b), RealScalar.ONE);
    // Tensor w = TensorWedge.of(a, b).multiply(RealScalar.of(-2));
    // TODO Eade suggests to treat the case b ~= -a separately!
    Tensor ab = TensorProduct.of(a, b);
    Tensor w = Transpose.of(ab).subtract(ab);
    Scalar c = RealScalar.ONE.add(a.dot(b)).reciprocal();
    return IdentityMatrix.of(a.length()).add(w).add(w.dot(w).multiply(c));
  }
}
