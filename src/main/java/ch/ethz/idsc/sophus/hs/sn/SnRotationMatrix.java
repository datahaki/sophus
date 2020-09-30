// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;

/** formula generalized from
 * "Rotation Between Two Vectors in R^3"
 * by Ethan Eade */
public enum SnRotationMatrix {
  ;
  private static final HsMemberQ HS_MEMBER_Q = SnMemberQ.of(Chop._10);

  /** function establishes orthogonal rotation matrix that rotates vector a onto b:
   * <pre>
   * SnRotationMatrix.of(a, b).dot(a) == b
   * </pre>
   * 
   * @param a vector with 2-norm equals to 1
   * @param b vector with 2-norm equals to 1
   * @return orthogonal matrix with determinant +1 (only for valid input a and b) with matrix . a == b
   * @throws Exception if either a or b are not of Euclidean length 1 */
  public static Tensor of(Tensor a, Tensor b) {
    // Tensor w = TensorWedge.of(a, b).multiply(RealScalar.of(-2));
    // TODO Eade suggests to treat the case b ~= -a separately!
    Tensor ab = TensorProduct.of( //
        HS_MEMBER_Q.requirePoint(a), //
        HS_MEMBER_Q.requirePoint(b));
    Tensor w = Transpose.of(ab).subtract(ab);
    Scalar c = RealScalar.ONE.add(a.dot(b)).reciprocal();
    return IdentityMatrix.of(a.length()).add(w).add(w.dot(w).multiply(c));
  }
}
