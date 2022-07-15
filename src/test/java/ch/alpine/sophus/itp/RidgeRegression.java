// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;

import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Transpose;
import ch.alpine.tensor.lie.Symmetrize;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.alpine.tensor.mat.pi.PseudoInverse;
import ch.alpine.tensor.sca.pow.Sqrt;

/** Reference:
 * "Machine Learning - A Probabilistic Perspective"
 * by Kevin P. Murphy, p. 226 */
/* package */ class RidgeRegression implements Serializable {
  private final HsDesign hsDesign;

  /** @param manifold non-null */
  public RidgeRegression(Manifold manifold) {
    hsDesign = new HsDesign(manifold);
  }

  /* package */ class Form2 implements Serializable {
    private final Tensor matrix;
    private final Tensor sigma_inverse;

    /** @param sequence of n anchor points
     * @param point */
    /* package */ Form2(Tensor sequence, Tensor point) {
      matrix = hsDesign.matrix(sequence, point);
      Scalar factor = RationalScalar.of(1, sequence.length());
      Tensor sigma = Transpose.of(matrix).dot(matrix).multiply(factor);
      // computation of pseudo inverse only may result in numerical deviation from true symmetric result
      sigma = sigma.add(DiagonalMatrix.of(sigma.length(), RealScalar.of(10)));
      sigma_inverse = Symmetrize.of(PseudoInverse.of(sigma).multiply(factor));
    }

    /** @return design matrix with n rows as log_x(p_i)
     * 
     * @see HsDesign */
    public Tensor matrix() {
      return matrix;
    }

    /** @return matrix that is symmetric positive definite if sequence contains sufficient points and
     * parameterization of tangent space is tight, for example SE(2). Otherwise symmetric positive
     * semidefinite matrix, for example S^d as embedded in R^(d+1).
     * 
     * @see PositiveDefiniteMatrixQ
     * @see PositiveSemidefiniteMatrixQ */
    public Tensor sigma_inverse() {
      return sigma_inverse;
    }

    /** @return diagonal of influence matrix */
    public Tensor leverages() {
      return Tensor.of(matrix.stream() //
          .map(v -> sigma_inverse.dot(v).dot(v)) //
          .map(Scalar.class::cast) //
          // theory guarantees that leverage is in interval [0, 1]
          // so far the numerics did not result in values below 0 here
          .map(Sqrt.FUNCTION));
    }
  }
}
