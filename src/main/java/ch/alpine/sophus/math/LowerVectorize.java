// code by jph
package ch.alpine.sophus.math;

import java.util.concurrent.atomic.AtomicInteger;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.TensorRuntimeException;
import ch.alpine.tensor.alg.TensorRank;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.HermitianMatrixQ;
import ch.alpine.tensor.mat.LowerTriangularize;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

/** lower vectorize is typically used on matrices that have a symmetry across the diagonal
 * such as {@link SymmetricMatrixQ}, {@link AntisymmetricMatrixQ}, {@link HermitianMatrixQ}
 * in order to remove the duplicate entries.
 * 
 * @see LowerTriangularize */
public enum LowerVectorize {
  ;
  /** @param tensor with rank at least 2
   * @param index for instance 0 to include diagonal elements, or -1 to exclude diagonal elements
   * @return vector
   * @throws Exception if given tensor is not a tensor of rank at least 2 */
  public static Tensor of(Tensor tensor, int index) {
    if (TensorRank.of(tensor) < 2)
      throw TensorRuntimeException.of(tensor);
    AtomicInteger atomicInteger = new AtomicInteger(index + 1);
    return Tensor.of(tensor.stream() //
        .flatMap(row -> row.stream().limit(atomicInteger.getAndIncrement())));
  }
}
