// code by jph
package ch.alpine.sophus.lie.u;

import java.io.Serializable;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Cache;

public class UAlgebra implements LieAlgebra, Serializable {
  private static final Cache<Integer, LieAlgebra> CACHE = Cache.of(UAlgebra::new, 8);

  /** @param n greater or equals to 2
   * @return */
  public static LieAlgebra of(int n) {
    return CACHE.apply(n);
  }

  // ---
  private final Tensor ad;

  private UAlgebra(int n) {
    ad = new MatrixAlgebra(basis(n)).ad();
  }

  @Override
  public Tensor ad() {
    return ad;
  }

  public static Tensor basis(int n) {
    Tensor tensor = Array.sparse(n * n, n, n);
    int index = 0;
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        tensor.set(RealScalar.ONE, index, i, j);
        tensor.set(RealScalar.ONE.negate(), index, j, i);
        ++index;
        tensor.set(ComplexScalar.I, index, i, j);
        tensor.set(ComplexScalar.I, index, j, i);
        ++index;
      }
    for (int i = 0; i < n; ++i) {
      tensor.set(ComplexScalar.I, index, i, i);
      ++index;
    }
    return tensor;
  }
}
