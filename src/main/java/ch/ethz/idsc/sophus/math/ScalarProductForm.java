// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.ArrayFlatten;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

public enum ScalarProductForm {
  ;
  /** @param p
   * @param q
   * @return */
  public static Tensor of(int p, int q) {
    if (q == 0)
      return IdentityMatrix.of(p);
    if (p == 0)
      return IdentityMatrix.of(q).negate();
    return ArrayFlatten.of(new Tensor[][] { //
        { IdentityMatrix.of(p), Array.zeros(p, q) }, //
        { Array.zeros(q, p), IdentityMatrix.of(q).negate() } });
  }
}
