// code by jph
package ch.ethz.idsc.sophus.math;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.ConjugateTranspose;
import ch.ethz.idsc.tensor.mat.InfluenceMatrix;
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
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39
 * 
 * @see InfluenceMatrix */
public class Mahalanobis implements Serializable {
  private final Tensor design;
  private final Tensor sigma;
  private final Tensor sigma_inverse;

  /** @param design matrix with n rows as log_x(p_i) */
  public Mahalanobis(Tensor design) {
    this.design = design;
    Scalar factor = RealScalar.of(design.length());
    sigma = ConjugateTranspose.of(design).dot(design).divide(factor);
    // computation of pseudo inverse only may result in numerical deviation from true symmetric result
    sigma_inverse = Symmetrize.of(PseudoInverse.of(sigma).divide(factor));
  }

  /** @return design matrix */
  public Tensor design() {
    return design;
  }

  /** @return design^H . design / Length[design] */
  public Tensor sigma_n() {
    return sigma;
  }

  /** @return matrix that is symmetric positive definite if sequence contains sufficient points and
   * parameterization of tangent space is tight, for example SE(2). Otherwise symmetric positive
   * semidefinite matrix, for example S^d as embedded in R^(d+1).
   * @see PositiveDefiniteMatrixQ
   * @see PositiveSemidefiniteMatrixQ */
  public Tensor sigma_inverse() {
    return sigma_inverse;
  }

  /** Function returns the same as
   * <pre>
   * InfluenceMatrix.of(design).leverages_sqrt()
   * </pre>
   * 
   * @return sqrt of diagonal of influence matrix in interval [0, 1]
   * @see InfluenceMatrix#leverages_sqrt() */
  public Tensor leverages() {
    return Tensor.of(design.stream().map(this::norm_squared));
  }

  /** Function returns the same as
   * <pre>
   * InfluenceMatrix.of(design).leverages_sqrt()
   * </pre>
   * 
   * @return sqrt of diagonal of influence matrix in interval [0, 1]
   * @see InfluenceMatrix#leverages_sqrt() */
  public Tensor leverages_sqrt() {
    return Tensor.of(design.stream().map(this::norm));
  }

  /** @param vector
   * @return sqrt of sigma_inverse . vector . vector */
  public Scalar norm_squared(Tensor vector) {
    // theory guarantees that leverage is in interval [0, 1]
    // so far the numerics did not result in values below 0 here
    return (Scalar) sigma_inverse.dot(vector).dot(vector);
  }

  /** @param vector
   * @return sqrt of sigma_inverse . vector . vector */
  public Scalar norm(Tensor vector) {
    return Sqrt.FUNCTION.apply(norm_squared(vector));
  }

  @Override // from Object
  public String toString() {
    return String.format("%s[%s]", getClass().getSimpleName(), Dimensions.of(design));
  }
}
