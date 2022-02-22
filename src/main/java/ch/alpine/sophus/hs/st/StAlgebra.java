// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.lie.so.SoAlgebra;

/** Algebra of Stiefel Manifold */
public enum StAlgebra {
  ;
  /** @param n
   * @param k not greater than n
   * @param degree of bch series
   * @return */
  public static HsAlgebra of(int n, int k, int degree) {
    if (n < k) // check is required
      throw new IllegalArgumentException(n + " " + k);
    return new HsAlgebra(SoAlgebra.of(n).ad(), n * k - k * (k + 1) / 2, degree);
  }
}
