// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/* package */ enum StaticHelper {
  ;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(i -> IdentityMatrix.of(i).negate(), 8);

  /** @param q
   * @return q * 2 - id */
  public static Tensor bic(Tensor q) {
    return q.add(CACHE.apply(q.length())).add(q);
  }
}
