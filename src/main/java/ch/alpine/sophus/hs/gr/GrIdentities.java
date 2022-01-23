// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.ad.MatrixBracket;

/** Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * by E. Batzies, K. Hueper, L. Machado, F. Silva Leite, 2015 */
public enum GrIdentities {
  ;
  /** @param p
   * @param v
   * @return [p, [p, v]] == v */
  public static Tensor of(Tensor p, Tensor v) {
    return MatrixBracket.of(p, MatrixBracket.of(p, v)).subtract(v);
  }
}
