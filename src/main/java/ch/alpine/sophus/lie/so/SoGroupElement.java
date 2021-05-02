// code by ob, jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.MatrixDotTranspose;
import ch.alpine.tensor.alg.Transpose;

/** Reference: http://ethaneade.com/lie.pdf */
public class SoGroupElement implements LieGroupElement {
  /** @param matrix orthogonal with determinant +1
   * @return */
  public static SoGroupElement of(Tensor matrix) {
    return new SoGroupElement(SoMemberQ.INSTANCE.require(matrix));
  }

  /***************************************************/
  private final Tensor matrix;

  private SoGroupElement(Tensor matrix) {
    this.matrix = matrix;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return matrix.unmodifiable();
  }

  @Override // from LieGroupElement
  public SoGroupElement inverse() {
    return new SoGroupElement(Transpose.of(matrix));
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor tensor) {
    return matrix.dot(SoMemberQ.INSTANCE.require(tensor));
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor v) { // v is skew with dimensions 3 x 3
    return matrix.dot(TSoMemberQ.INSTANCE.require(v)); // consistent with So3Transport
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor v) { // v is skew with dimensions 3 x 3
    return MatrixDotTranspose.of(dL(v), matrix);
  }
}
