// code by jph
package ch.alpine.sophus.lie.gl;

import java.io.Serializable;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.sophus.lie.so3.So3Geodesic;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Inverse;
import ch.alpine.tensor.mat.SquareMatrixQ;

/** Element of the Lie group GL(n) of invertible square matrices.
 * 
 * implementation is not optimized. for instance
 * new LinearGroupElement(a).inverse().combine(b)
 * new LinearGroupElement(LinearSolve.of(a, b))
 * 
 * @see So3Geodesic */
public class GlGroupElement implements LieGroupElement, Serializable {
  /** @param matrix square and invertible
   * @return
   * @throws Exception if given matrix is not invertible */
  public static GlGroupElement of(Tensor matrix) {
    return new GlGroupElement(matrix, Inverse.of(matrix));
  }

  // ---
  private final Tensor matrix;
  private final Tensor inverse;

  private GlGroupElement(Tensor matrix, Tensor inverse) {
    this.matrix = matrix;
    this.inverse = inverse;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return matrix.unmodifiable();
  }

  @Override // from LieGroupElement
  public GlGroupElement inverse() {
    return new GlGroupElement(inverse, matrix);
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor tensor) {
    return matrix.dot(SquareMatrixQ.require(tensor));
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor v) { // v is square
    return dL(v).dot(inverse);
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor v) { // v is square
    return matrix.dot(SquareMatrixQ.require(v));
  }
}
