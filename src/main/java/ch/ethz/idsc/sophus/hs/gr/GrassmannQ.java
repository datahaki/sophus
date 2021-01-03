// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.mat.HermitianMatrixQ;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;

/** checks if matrix is hermitian and idempotent
 * 
 * @see HermitianMatrixQ */
public enum GrassmannQ {
  ;
  /** @param matrix
   * @param chop
   * @return */
  public static boolean of(Tensor matrix, Chop chop) {
    return SymmetricMatrixQ.of(matrix, chop) // P = P'
        && IdempotentQ.of(matrix, chop); // P . P == P
  }

  /** @param matrix
   * @return */
  public static boolean of(Tensor matrix) {
    return of(matrix, Tolerance.CHOP);
  }

  /** @param matrix
   * @param chop
   * @return */
  public static Tensor require(Tensor matrix, Chop chop) {
    if (of(matrix, chop))
      return matrix;
    throw TensorRuntimeException.of(matrix);
  }

  /** @param matrix
   * @return */
  public static Tensor require(Tensor matrix) {
    if (of(matrix))
      return matrix;
    throw TensorRuntimeException.of(matrix);
  }
}
