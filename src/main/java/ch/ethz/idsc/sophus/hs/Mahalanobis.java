// code by jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.ConjugateTranspose;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** The reference suggests to use the inverse and the biinvariant mean m as reference point.
 * For our more general purposes, we employ the pseudo-inverse of the form evaluated at an
 * arbitrary point.
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39 */
public class Mahalanobis implements Serializable {
  private final Tensor matrix;
  private final Tensor sigma;
  private final Tensor sigma_inverse;

  /** @param matrix design
   * @see HsDesign */
  public Mahalanobis(Tensor matrix) {
    this.matrix = matrix;
    Scalar factor = RationalScalar.of(1, matrix.length());
    sigma = ConjugateTranspose.of(matrix).dot(matrix).multiply(factor);
    // if matrix has full rank, CholeskyDecomposition would be more efficient
    // computation of pseudo inverse only may result in numerical deviation from true symmetric result
    sigma_inverse = Symmetrize.of(PseudoInverse.of(sigma).multiply(factor));
  }

  /** @return design matrix with n rows as log_x(p_i)
   * 
   * @see HsDesign */
  public Tensor matrix() {
    return matrix;
  }

  /** @return */
  public Tensor sigma_n() {
    return sigma;
  }

  /** @return matrix that is symmetric positive definite if sequence contains sufficient points and
   * parameterization of tangent space is tight, for example SE(2). Otherwise symmetric positive
   * semidefinite matrix, for example S^d as embedded in R^(d+1).
   * 
   * @see PositiveDefiniteMatrixQ
   * @see PositiveSemidefiniteMatrixQ */
  public Tensor sigma_inverse() {
    return sigma_inverse.unmodifiable();
  }

  /** @param vector
   * @return sqrt of sigma_inverse . vector . vector */
  private Scalar norm(Tensor vector) {
    // theory guarantees that leverage is in interval [0, 1]
    // so far the numerics did not result in values below 0 here
    return Sqrt.FUNCTION.apply(sigma_inverse.dot(vector).dot(vector).Get());
  }

  /** @param vector
   * @return */
  public Scalar distance(Tensor vector) {
    return norm(vector);
  }

  /** @return sqrt of diagonal of influence matrix */
  public Tensor leverages_sqrt() {
    return Tensor.of(matrix.stream().map(this::norm));
  }
}
