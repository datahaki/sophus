// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.Orthogonalize;

public enum TSnProjection {
  ;
  private static final Cache<Integer, Tensor> CACHE = Cache.of(IdentityMatrix::of, 10);

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
