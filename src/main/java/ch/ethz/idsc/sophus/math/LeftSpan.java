// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.LeastSquares;
import ch.ethz.idsc.tensor.mat.SingularValueDecomposition;
import ch.ethz.idsc.tensor.mat.Tolerance;

/** does not have equivalent in Mathematica
 * 
 * @see LeastSquares */
public enum LeftSpan {
  ;
  private static final Scalar _0 = RealScalar.of(0.0);
  private static final Scalar _1 = RealScalar.of(1.0);

  /** function returns a vector vnull that satisfies
   * vnull . matrix == 0
   * 
   * @param vector
   * @param matrix transpose of matrix
   * @return vector . (I - matrix . matrix^+) */
  public static Tensor kernel(Tensor vector, Tensor matrix) {
    return vector.subtract(image(vector, matrix));
  }

  /** function returns a vector vimage that satisfies
   * vimage . matrix == vector . matrix
   * 
   * @param vector
   * @param matrix
   * @return vector . matrix . matrix^+ */
  public static Tensor image(Tensor vector, Tensor matrix) {
    SingularValueDecomposition svd = SingularValueDecomposition.of(matrix);
    Tensor u = svd.getU();
    Tensor kron = Tensor.of(svd.values().stream() //
        .map(Scalar.class::cast) //
        .map(LeftSpan::unitize_chop));
    // could still optimize further by extracting elements from rows in u
    // Tensor U = Tensor.of(u.stream().map(kron::pmul)); // extract instead of pmul!
    // return U.dot(vector.dot(U));
    return u.dot(kron.pmul(vector.dot(u)));
  }

  private static Scalar unitize_chop(Scalar scalar) {
    return Scalars.isZero(Tolerance.CHOP.apply(scalar)) ? _0 : _1;
  }
}
