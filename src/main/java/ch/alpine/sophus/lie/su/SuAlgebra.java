// code by jph
package ch.alpine.sophus.lie.su;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.math.bch.BakerCampbellHausdorff;
import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.sca.N;

public class SuAlgebra implements LieAlgebra, Serializable {
  private static final Cache<Integer, LieAlgebra> CACHE = Cache.of(SuAlgebra::new, 8);

  /** @param n greater or equals to 2
   * @return */
  public static LieAlgebra of(int n) {
    return CACHE.apply(n);
  }

  // ---
  private final int n;
  private final Tensor ad;

  private SuAlgebra(int n) {
    this.n = n;
    ad = new MatrixAlgebra(basis()).ad();
  }

  @Override
  public Tensor ad() {
    return ad;
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(N.DOUBLE.of(ad), degree);
  }

  @Override
  public Tensor basis() {
    Tensor tensor = Array.sparse(n * n - 1, n, n);
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
    for (int i = 1; i < n; ++i) {
      int j = i - 1;
      tensor.set(ComplexScalar.I, index, j, j);
      tensor.set(ComplexScalar.I.negate(), index, i, i);
      ++index;
    }
    return tensor;
  }
}
