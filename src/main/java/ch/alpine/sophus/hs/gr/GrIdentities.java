// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.lie.MatrixBracket;
import ch.alpine.tensor.Tensor;

/** Reference:
 * "Geometric mean and geodesic regression on Grassmannians"
 * by E. Batzies, K. Hueper, L. Machado, F. Silva Leite, 2015 */
public enum GrIdentities {
  ;
  /** TODO SOPHUS document params
   * 
   * @param p
   * @param v
   * @return [p, [p, v]] == v */
  public static Tensor of(Tensor p, Tensor v) {
    // see TGrMemberQ::projection
    return MatrixBracket.of(p, MatrixBracket.of(p, v)).subtract(v);
  }
}
