// code by jph
package ch.alpine.sophus.hs.gr;

import java.io.Serializable;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.MatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

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
    Integers.requireEquals(n, v.length());
    return Tensors.matrix((i, j) -> (i < k && k <= j) || (j < k && k <= i) //
        ? v.Get(i, j).add(v.Get(j, i)).multiply(RationalScalar.HALF)
        : v.Get(i, j).zero(), n, n);
  }
}
