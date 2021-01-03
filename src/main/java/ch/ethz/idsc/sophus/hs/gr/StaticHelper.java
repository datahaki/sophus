// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.HsInfluence;
import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.TensorWedge;

/** Reference:
 * geomstats - grassmannian.py */
/* package */ enum StaticHelper {
  ;
  /** @param vector
   * @return matrix that projects points to line spanned by vector */
  public static Tensor projection1(Tensor vector) {
    return projection(Tensor.of(vector.stream().map(Tensors::of)));
  }

  /** @param matrix design
   * @return */
  public static Tensor projection(Tensor matrix) {
    return new HsInfluence(matrix).matrix();
  }

  /** @param x in Gr(n, k)
   * @param v square matrix
   * @return v projected to tangent space at x */
  public static Tensor projectTangent(Tensor x, Tensor v) {
    return MatrixBracket.of(x, TensorWedge.of(v));
  }
}
