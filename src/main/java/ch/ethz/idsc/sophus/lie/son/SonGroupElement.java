// code by ob, jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** Reference: http://ethaneade.com/lie.pdf */
public class SonGroupElement implements LieGroupElement {
  private static final HsMemberQ HS_MEMBER_Q = SonMemberQ.of(Tolerance.CHOP);

  /** @param matrix orthogonal with determinant +1
   * @return */
  public static SonGroupElement of(Tensor matrix) {
    return new SonGroupElement(HS_MEMBER_Q.requirePoint(matrix));
  }

  /***************************************************/
  private final Tensor matrix;

  private SonGroupElement(Tensor matrix) {
    this.matrix = matrix;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return matrix.unmodifiable();
  }

  @Override // from LieGroupElement
  public SonGroupElement inverse() {
    return new SonGroupElement(Transpose.of(matrix));
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor tensor) {
    return matrix.dot(HS_MEMBER_Q.requirePoint(tensor));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor v) { // v is skew with dimensions 3 x 3
    return matrix.dot(HS_MEMBER_Q.requireTangent(IdentityMatrix.of(matrix.length()), v)); // consistent with So3Transport
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor v) { // v is skew with dimensions 3 x 3
    return dL(v).dot(Transpose.of(matrix));
  }
}
