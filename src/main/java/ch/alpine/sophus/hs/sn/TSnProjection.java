// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Join;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.Orthogonalize;

public enum TSnProjection {
  ;
  private static final int CACHE_SIZE = 10;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(IdentityMatrix::of, CACHE_SIZE);

  /** @param p vector of length n
   * @return matrix of size n-1 x n that satisfies matrix . x == 0 */
  public static Tensor of(Tensor p) {
    return unsafe(SnMemberQ.INSTANCE.require(p));
  }

  /* package */ static Tensor unsafe(Tensor p) {
    int n = p.length();
    return Tensor.of(Orthogonalize.of(Join.of(Tensors.of(p), CACHE.apply(n))).stream().skip(1).limit(n - 1));
  }
}
