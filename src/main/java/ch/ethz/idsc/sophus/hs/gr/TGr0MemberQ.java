// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.MatrixQ;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class TGr0MemberQ implements MemberQ, Serializable {
  private final int n;
  private final int k;

  public TGr0MemberQ(int n, int k) {
    this.n = n;
    this.k = k;
    Integers.requirePositiveOrZero(Math.subtractExact(n, k));
  }

  @Override
  public boolean test(Tensor matrix) {
    return MatrixQ.ofSize(matrix, n, n) //
        && SymmetricMatrixQ.of(matrix) //
        && Chop._08.allZero(Tensor.of(matrix.stream().limit(k).map(row -> row.extract(0, k)))) //
        && Chop._08.allZero(Tensor.of(matrix.stream().skip(k).map(row -> row.extract(k, n))));
  }

  public Tensor project(Tensor v) {
    if (n == v.length())
      return Tensors.matrix((i, j) -> (i < k && k <= j) || (j < k && k <= i) //
          ? v.Get(i, j).add(v.Get(j, i)).multiply(RationalScalar.HALF)
          : v.Get(i, j).zero(), n, n);
    throw TensorRuntimeException.of(v);
  }
}
