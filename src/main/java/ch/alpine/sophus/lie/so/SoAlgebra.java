// code by jph
package ch.alpine.sophus.lie.so;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.lie.ad.BakerCampbellHausdorff;
import ch.alpine.tensor.lie.ad.MatrixAlgebra;

/** Careful:
 * does not match {@link So3Algebra} in the special case when n == 3 */
public class SoAlgebra implements LieAlgebra, Serializable {
  private static final Cache<Integer, LieAlgebra> CACHE = Cache.of(SoAlgebra::new, 8);

  /** @param n greater or equals to 2
   * @return */
  public static LieAlgebra of(int n) {
    return CACHE.apply(n);
  }

  // ---
  private final int n;
  private final MatrixAlgebra matrixAlgebra;

  private SoAlgebra(int n) {
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
    Tensor tensor = Array.sparse(n * (n - 1) / 2, n, n);
    int index = 0;
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        {
          tensor.set(RealScalar.ONE, index, i, j);
          tensor.set(RealScalar.ONE.negate(), index, j, i);
          ++index;
        }
      }
    return tensor;
  }

  @Override
  public String toString() {
    return String.format("%s[%d]", getClass().getSimpleName(), n);
  }
}
