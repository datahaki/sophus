// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** formula generalized from
 * "Rotation Between Two Vectors in R^3"
 * by Ethan Eade */
public class SnAction implements TensorUnaryOperator {
  private final Tensor g;

  public SnAction(Tensor g) {
    this.g = g;
  }

  @Override
  public Tensor apply(Tensor p) {
    return g.dot(p);
  }

  /** function establishes orthogonal rotation matrix that rotates vector a onto b:
   * <pre>
   * SnRotationMatrix.of(a, b).dot(a) == b
   * </pre>
   * 
   * @param a vector with 2-norm equals to 1
   * @param b vector with 2-norm equals to 1
   * @return orthogonal matrix with determinant +1 (only for valid input a and b) with matrix . a == b
   * @throws Exception if either a or b are not of Euclidean length 1 */
  public static Tensor match(Tensor a, Tensor b) {
    // Tensor w = TensorWedge.of(a, b).multiply(RealScalar.of(-2));
    // TODO Eade suggests to treat the case b ~= -a separately!
    Tensor ab = TensorProduct.of( //
        SnMemberQ.INSTANCE.require(a), //
        SnMemberQ.INSTANCE.require(b));
    Tensor w = Transpose.of(ab).subtract(ab);
    Scalar c = RealScalar.ONE.add(a.dot(b)).reciprocal();
    return IdentityMatrix.of(a.length()).add(w).add(w.dot(w).multiply(c));
  }
}
