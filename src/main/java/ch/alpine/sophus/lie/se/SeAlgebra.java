// code by jph
package ch.alpine.sophus.lie.se;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.sophus.lie.LieAlgebra;
import ch.alpine.sophus.lie.MatrixAlgebra;
import ch.alpine.sophus.lie.se3.Se3Algebra;
import ch.alpine.sophus.lie.so.SoAlgebra;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.io.MathematicaFormat;
import ch.alpine.tensor.lie.bch.BakerCampbellHausdorff;

/** Careful:
 * TODO SOPHUS does not match {@link Se3Algebra} in the special case when n == 3
 * 
 * @see SoAlgebra */
public class SeAlgebra implements LieAlgebra, Serializable {
  private static final Cache<Integer, LieAlgebra> CACHE = Cache.of(SeAlgebra::new, 8);

  /** @param n greater or equals to 2
   * @return */
  public static LieAlgebra of(int n) {
    return CACHE.apply(n);
  }

  // ---
  private final int n;
  private final MatrixAlgebra matrixAlgebra;

  private SeAlgebra(int n) {
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
    Tensor tensor = Array.sparse(n + n * (n - 1) / 2, n + 1, n + 1);
    int index = 0;
    for (int i = 0; i < n; ++i) {
      tensor.set(RealScalar.ONE, index, i, n);
      ++index;
    }
    for (int i = 0; i < n; ++i)
      for (int j = i + 1; j < n; ++j) {
        tensor.set(RealScalar.ONE, index, i, j);
        tensor.set(RealScalar.ONE.negate(), index, j, i);
        ++index;
      }
    return tensor;
  }

  @Override
  public String toString() {
    return MathematicaFormat.concise("SeAlgebra", n);
  }
}
