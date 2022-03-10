// code by jph
package ch.alpine.sophus.lie.sl;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Cache;

public class SlAlgebra implements LieAlgebra, Serializable {
  private static final Cache<Integer, LieAlgebra> CACHE = Cache.of(SlAlgebra::new, 8);

  /** @param n greater or equals to 2
   * @return */
  public static LieAlgebra of(int n) {
    return CACHE.apply(n);
  }

  private final int n;
  private final MatrixAlgebra matrixAlgebra;

  private SlAlgebra(int n) {
    if (n < 2)
      throw new IllegalArgumentException("n=" + n);
    this.n = n;
    matrixAlgebra = new MatrixAlgebra(basis());
  }

  @Override
  public Tensor ad() {
    return matrixAlgebra.ad().copy();
  }

  @Override
  public BinaryOperator<Tensor> bch(int degree) {
    return BakerCampbellHausdorff.of(ad(), degree);
  }

  @Override
  public Tensor basis() {
    Tensor tensor = Array.sparse(n * n - 1, n, n);
    int index = 0;
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        {
          tensor.set(RealScalar.ONE, index, i, j);
          tensor.set(RealScalar.ONE.negate(), index, j, i);
          ++index;
        }
        {
          tensor.set(RealScalar.ONE, index, i, j);
          tensor.set(RealScalar.ONE, index, j, i);
          ++index;
        }
      }
    for (int i = 1; i < n; ++i) {
      tensor.set(RealScalar.ONE, index, i - 1, i - 1);
      tensor.set(RealScalar.ONE.negate(), index, i, i);
      ++index;
    }
    return tensor.multiply(RationalScalar.HALF);
  }

  @Override
  public String toString() {
    return String.format("%s[%d]", getClass().getSimpleName(), n);
  }
}
