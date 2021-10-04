// code by jph
package ch.alpine.sophus.math;

import java.util.concurrent.atomic.AtomicInteger;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.mat.LowerTriangularize;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

/** lower vectorize is typically used on matrices that have a symmetry across the diagonal
 * such as {@link SymmetricMatrixQ}, {@link AntisymmetricMatrixQ}, {@link HermitianMatrixQ}
 * in order to remove the duplicate entries.
 * 
 * @see LowerTriangularize */
public enum LowerVectorize {
  ;
  /** @param matrix
   * @param index for instance 0 to include diagonal elements, or -1 to exclude diagonal elements
   * @return vector
   * @throws Exception if given matrix is not a tensor of rank at least 2 */
  public static Tensor of(Tensor matrix, int index) {
    MatrixQ.require(matrix);
    AtomicInteger atomicInteger = new AtomicInteger(index + 1);
    return Tensor.of(matrix.stream() //
        .flatMap(row -> row.stream().limit(atomicInteger.getAndIncrement())));
  }
}
