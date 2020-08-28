// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.lie.MatrixLog;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;

/* package */ enum StaticHelper {
  ;
  private static final int MAX_ITERATIONS = 500;

  /** @param x
   * @return matrix that projects points to line spanned by x */
  public static Tensor projection(Tensor x) {
    x = Normalize.with(Norm._2).apply(x);
    return TensorProduct.of(x, x);
  }

  /** @param matrix square
   * @return
   * @throws Exception if given matrix is non-square */
  /* package */ static Tensor _log(Tensor matrix) {
    if (matrix.length() == 2)
      return MatrixLog.of(matrix);
    Tensor x = matrix.subtract(IdentityMatrix.of(matrix.length()));
    Tensor nxt = x;
    Tensor sum = nxt;
    for (int k = 2; k < MAX_ITERATIONS; ++k) {
      nxt = nxt.dot(x);
      Tensor prv = sum;
      sum = sum.add(nxt.divide(RealScalar.of(k % 2 == 0 ? -k : k)));
      if (Chop.NONE.isClose(sum, prv))
        return sum;
    }
    System.err.println("CONVERGENCE FAILURE");
    throw TensorRuntimeException.of(matrix); // insufficient convergence
  }
}
