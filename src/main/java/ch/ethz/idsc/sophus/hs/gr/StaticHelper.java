// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.lie.TensorProduct;
import ch.ethz.idsc.tensor.lie.TensorWedge;
import ch.ethz.idsc.tensor.red.Norm;

/** Reference:
 * geomstats - grassmannian.py */
/* package */ enum StaticHelper {
  ;
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  /** @param x
   * @return matrix that projects points to line spanned by x */
  public static Tensor projection(Tensor x) {
    x = NORMALIZE.apply(x);
    return TensorProduct.of(x, x);
  }

  /** @param x base point
   * @param v vector
   * @return */
  /* package */ static Tensor projectTangent(Tensor x, Tensor v) {
    return MatrixBracket.of(x, TensorWedge.of(v));
  }
}
