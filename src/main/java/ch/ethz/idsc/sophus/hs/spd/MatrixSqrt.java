// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.mat.Eigensystem;
import ch.ethz.idsc.tensor.mat.MatrixPower;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** Determines the square root of a symmetric positive definite matrix.
 * 
 * Reference:
 * "Riemannian Geometric Statistics in Medical Image Analysis", 2020
 * Edited by Xavier Pennec, Sommer, P. Thomas Fletcher, p. 80
 * 
 * @see MatrixPower */
/* package */ class MatrixSqrt implements Serializable {
  /** @param matrix symmetric
   * @return
   * @throws Exception if matrix is not symmetric */
  public static MatrixSqrt ofSymmetric(Tensor matrix) {
    return new MatrixSqrt(matrix);
  }

  /***************************************************/
  private final Tensor avec;
  private final Tensor ainv;
  private final Tensor sqrt;

  private MatrixSqrt(Tensor matrix) {
    Eigensystem eigensystem = Eigensystem.ofSymmetric(matrix);
    avec = eigensystem.vectors();
    ainv = Transpose.of(avec);
    sqrt = eigensystem.values().map(Sqrt.FUNCTION);
  }

  /** @return square root of given matrix */
  public Tensor forward() {
    return ainv.dot(sqrt.pmul(avec));
  }

  /** @return inverse of forward() */
  public Tensor inverse() {
    return ainv.dot(sqrt.map(Scalar::reciprocal).pmul(avec));
  }
}
