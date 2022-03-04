// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.hs.ad.HsAlgebra;
import ch.alpine.sophus.lie.ad.MatrixAlgebra;
import ch.alpine.sophus.lie.se3.Se3Algebra;
import ch.alpine.sophus.lie.so.SoAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.PackageTestAccess;

/** Careful:
 * does not match {@link Se3Algebra} in the special case when n == 3
 * 
 * @see SoAlgebra */
public enum GrAlgebra {
  ;
  /** @param n
   * @param k
   * @param degree
   * @return */
  public static HsAlgebra of(int n, int k, int degree) {
    return new HsAlgebra(new MatrixAlgebra(basis(n, k)).ad(), k * (n - k), degree);
  }

  @PackageTestAccess
  static Tensor basis(int n, int k) {
    Tensor tensor = Array.sparse(n * (n - 1) / 2, n, n);
    int index = 0;
    for (int i = 0; i < k; ++i)
      for (int j = k; j < n; ++j) {
        tensor.set(RealScalar.ONE, index, i, j);
        tensor.set(RealScalar.ONE.negate(), index, j, i);
        ++index;
      }
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j)
        if (k <= i || j < k) {
          tensor.set(RealScalar.ONE, index, i, j);
          tensor.set(RealScalar.ONE.negate(), index, j, i);
          ++index;
        }
    return tensor;
  }
}
