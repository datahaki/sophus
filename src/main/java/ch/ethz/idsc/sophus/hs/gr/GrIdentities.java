// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.lie.MatrixBracket;
import ch.ethz.idsc.tensor.Tensor;

public enum GrIdentities {
  ;
  /** @param p
   * @param v
   * @return [p, [p, v]] == v */
  public static Tensor of(Tensor p, Tensor v) {
    return MatrixBracket.of(p, MatrixBracket.of(p, v)).subtract(v);
  }
}
