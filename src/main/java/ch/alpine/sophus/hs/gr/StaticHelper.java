// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.mat.IdentityMatrix;

/* package */ enum StaticHelper {
  ;
  private static final int CACHE_SIZE = 8;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(i -> IdentityMatrix.of(i).negate(), CACHE_SIZE);

  /** @param q
   * @return q * 2 - id */
  public static Tensor bic(Tensor q) {
    return q.add(CACHE.apply(q.length())).add(q);
  }
}
