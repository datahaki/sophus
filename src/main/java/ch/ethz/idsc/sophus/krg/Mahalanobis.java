// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.HsDesign;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.lie.Symmetrize;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PositiveSemidefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.sca.Sqrt;

/** The reference suggests to use the inverse and the biinvariant mean m as reference point.
 * For our more general purposes, we employ the pseudo-inverse of the form evaluated at an
 * arbitrary point.
 * 
 * <p>Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, 2012, p. 39 */
public class Mahalanobis implements Serializable {
  private final HsDesign hsDesign;

  /** @param vectorLogManifold non-null */
  public Mahalanobis(VectorLogManifold vectorLogManifold) {
    hsDesign = new HsDesign(vectorLogManifold);
  }

  public class Form implements Serializable {
    private final Tensor matrix;
    private final Tensor sigma_inverse;

    /** @param sequence of n anchor points
     * @param point */
    public Form(Tensor sequence, Tensor point) {
      matrix = hsDesign.matrix(sequence, point);
      Scalar factor = RationalScalar.of(1, sequence.length());
      Tensor sigma = Transpose.of(matrix).dot(matrix).multiply(factor);
      // computation of pseudo inverse only may result in numerical deviation from true symmetric result
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

    /** @param variogram
     * @return */
    public Tensor leverages(ScalarUnaryOperator variogram) {
      return Tensor.of(matrix.stream() //
          .map(v -> sigma_inverse.dot(v).dot(v)) //
          .map(Scalar.class::cast) //
          .map(Sqrt.FUNCTION) //
          .map(variogram));
    }
  }
}
