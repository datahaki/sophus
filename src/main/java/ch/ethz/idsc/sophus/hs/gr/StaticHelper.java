// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.TensorWedge;

/** Reference:
 * geomstats - grassmannian.py */
/* package */ enum StaticHelper {
  ;
  /** @param x in Gr(n, k)
   * @param v square matrix
   * @return v projected to tangent space at x */
  public static Tensor projectTangent(Tensor x, Tensor v) {
    return MatrixBracket.of(x, TensorWedge.of(v));
  }
}
