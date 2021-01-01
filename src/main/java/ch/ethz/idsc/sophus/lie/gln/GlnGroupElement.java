// code by jph
package ch.ethz.idsc.sophus.lie.gln;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.sophus.lie.so3.So3Geodesic;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Inverse;
import ch.ethz.idsc.tensor.mat.SquareMatrixQ;

/** Element of the Lie group GL(n) of invertible square matrices.
 * 
 * implementation is not optimized. for instance
 * new LinearGroupElement(a).inverse().combine(b)
 * new LinearGroupElement(LinearSolve.of(a, b))
 * 
 * @see So3Geodesic */
public class GlnGroupElement implements LieGroupElement, Serializable {
  private static final long serialVersionUID = 6859946688709959077L;

  /** @param matrix square and invertible
   * @return
   * @throws Exception if given matrix is not invertible */
  public static GlnGroupElement of(Tensor matrix) {
    return new GlnGroupElement(matrix, Inverse.of(matrix));
  }

  /***************************************************/
  private final Tensor matrix;
  private final Tensor inverse;

  private GlnGroupElement(Tensor matrix, Tensor inverse) {
    this.matrix = matrix;
    this.inverse = inverse;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return matrix.unmodifiable();
  }

  @Override // from LieGroupElement
  public GlnGroupElement inverse() {
    return new GlnGroupElement(inverse, matrix);
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
