// code by ob, jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.MatrixQ;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** Reference: http://ethaneade.com/lie.pdf */
public class So3GroupElement implements LieGroupElement {
  /** @param matrix orthogonal 3 x 3 with determinant +1
   * @return */
  public static So3GroupElement of(Tensor matrix) {
    if (matrix.length() == 3)
      return new So3GroupElement(requireSO(matrix));
    throw TensorRuntimeException.of(matrix);
  }

  /***************************************************/
  private final Tensor matrix;

  private So3GroupElement(Tensor matrix) {
    this.matrix = matrix;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return matrix.unmodifiable();
  }

  @Override // from LieGroupElement
  public So3GroupElement inverse() {
    return new So3GroupElement(Transpose.of(matrix));
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor tensor) {
    return matrix.dot(requireSO(tensor));
  }

  @Override
  public Tensor dL(Tensor v) {
    return matrix.dot(MatrixQ.requireSize(v, 3, 3)); // consistent with So3Transport
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor tensor) {
    return matrix.dot(tensor);
  }

  /** @param tensor
   * @return given tensor
   * @throws Exception if given tensor is not an orthogonal matrix with determinant +1 */
  private static Tensor requireSO(Tensor tensor) {
    Tolerance.CHOP.requireClose(Det.of(tensor), RealScalar.ONE);
    return OrthogonalMatrixQ.require(tensor);
  }
}
