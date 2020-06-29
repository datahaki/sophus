// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.SingularValueDecomposition;
import ch.ethz.idsc.tensor.mat.Tolerance;

/* package */ enum SpanProjection {
  ;
  private static final Scalar _0 = RealScalar.of(0.0);
  private static final Scalar _1 = RealScalar.of(1.0);

  /** @param matrix
   * @param vector
   * @return (I - matrix^+ . matrix) . vector */
  public static Tensor kernel(Tensor matrix, Tensor vector) {
    return vector.subtract(image(matrix, vector));
  }

  /** @param matrix
   * @param vector
   * @return matrix^+ . matrix . vector */
  public static Tensor image(Tensor matrix, Tensor vector) {
    SingularValueDecomposition svd = SingularValueDecomposition.of(matrix);
    Tensor u = svd.getU();
    Tensor kron = Tensor.of(svd.values().stream() //
        .map(Scalar.class::cast) //
        .map(SpanProjection::unitize_chop));
    return u.dot(kron.pmul(vector.dot(u)));
  }

  private static Scalar unitize_chop(Scalar scalar) {
    return Scalars.isZero(Tolerance.CHOP.apply(scalar)) ? _0 : _1;
  }
}
