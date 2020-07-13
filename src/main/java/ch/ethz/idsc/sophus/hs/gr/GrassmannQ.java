// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.mat.HermitianMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Chop;

public enum GrassmannQ {
  ;
  public static boolean of(Tensor matrix, Chop chop) {
    return HermitianMatrixQ.of(matrix) //
        && chop.close(matrix, matrix.dot(matrix)); // idempotent
  }

  public static boolean of(Tensor matrix) {
    return of(matrix, Tolerance.CHOP);
  }

  public static Tensor require(Tensor matrix) {
    if (of(matrix))
      return matrix;
    throw TensorRuntimeException.of(matrix);
  }
}
