// code by ob, jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** Reference: http://ethaneade.com/lie.pdf */
public class SonGroupElement implements LieGroupElement {
  /** @param matrix orthogonal with determinant +1
   * @return */
  public static SonGroupElement of(Tensor matrix) {
    return new SonGroupElement(SonMemberQ.INSTANCE.require(matrix));
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
    return matrix.dot(SonMemberQ.INSTANCE.require(tensor));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor v) { // v is skew with dimensions 3 x 3
    return matrix.dot(new TSonMemberQ(IdentityMatrix.of(matrix.length())).require(v)); // consistent with So3Transport
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor v) { // v is skew with dimensions 3 x 3
    return dL(v).dot(Transpose.of(matrix));
  }
}
