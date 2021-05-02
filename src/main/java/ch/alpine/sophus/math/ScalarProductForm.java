// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ArrayFlatten;
import ch.alpine.tensor.mat.IdentityMatrix;

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
