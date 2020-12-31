// code by jph
package ch.ethz.idsc.sophus.ga;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.alg.Reverse;
import ch.ethz.idsc.tensor.alg.Subsets;
import ch.ethz.idsc.tensor.alg.Transpose;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.io.ScalarArray;
import ch.ethz.idsc.tensor.lie.MatrixExp;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.sca.Chop;

/** geometric algebra */
public class CliffordAlgebra {
  private static final Scalar[] SIGN = { RealScalar.ONE, RealScalar.ONE.negate() };
  private static final int MAX_SIZE = 6;
  private static final Function<Integer, CliffordAlgebra> CACHE = Cache.of(CliffordAlgebra::new, MAX_SIZE);

  /** @param n
   * @return */
  public static CliffordAlgebra of(int n) {
    return CACHE.apply(Integers.requirePositiveOrZero(n));
  }

  /***************************************************/
  private final Tensor gp;
  private final Tensor cp;
  private final Tensor reverse;

  private CliffordAlgebra(int n) {
    Tensor range = Range.of(0, n);
    int m = 1 << n;
    List<Tensor> list = new ArrayList<>(m);
    Map<Tensor, Integer> map = new HashMap<>();
    for (int k = 0; k <= n; ++k)
      for (Tensor perm : Subsets.of(range, k)) {
        list.add(perm);
        map.put(perm, map.size());
      }
    gp = Array.zeros(m, m, m);
    for (int i = 0; i < m; ++i)
      for (int j = 0; j < m; ++j) {
        SignedSubset signedSubset = new SignedSubset(Join.of(list.get(i), list.get(j)));
        gp.set(signedSubset.sign, map.get(signedSubset.normal), j, i);
      }
    cp = gp.subtract(Transpose.of(gp, 0, 2, 1)).multiply(RationalScalar.HALF);
    reverse = Tensor.of(IntStream.range(0, m).mapToObj(k -> gp.Get(0, k, k)));
    Tensor _reverse = Tensor.of(list.stream() // only for validation
        .map(Reverse::of) //
        .map(SignedSubset::new) //
        .map(SignedSubset::sign));
    Chop.NONE.requireClose(reverse, _reverse);
  }

  /** @return geometric product tensor of rank 3 */
  public Tensor gp() {
    return gp.unmodifiable();
  }

  public Tensor gp(Tensor x, Tensor y) {
    return gp.dot(x).dot(y);
  }

  /** @return commutator product tensor of rank 3 */
  public Tensor cp() {
    return cp.unmodifiable();
  }

  /** @param x multivector
   * @return */
  public Tensor reverse(Tensor x) {
    return x.pmul(reverse);
  }

  /** @param x multivector
   * @return
   * @throws Exception if x cannot be inverted */
  public Tensor reciprocal(Tensor x) {
    return LinearSolve.of(gp.dot(x), UnitVector.of(gp.length(), 0));
  }

  /** @param x multivector
   * @return */
  public Tensor exp(Tensor x) {
    return MatrixExp.of(gp.dot(x)).get(Tensor.ALL, 0);
  }

  // only for testing
  /* package */ Tensor _exp(Tensor a) {
    Tensor sum = UnitVector.of(gp.length(), 0);
    Tensor p = sum;
    for (int k = 1; k < 40; ++k) {
      p = gp.dot(p).dot(a).divide(RealScalar.of(k));
      sum = sum.add(p);
      if (Chop._40.allZero(p))
        break;
    }
    return sum;
  }

  // TODO implementation not as efficient as could be
  private static class SignedSubset {
    private final Scalar sign;
    private final Tensor normal;

    public SignedSubset(Tensor a) {
      Scalar[] scalars = ScalarArray.ofVector(a);
      boolean flag = true;
      int m = scalars.length - 1;
      int swaps = 0;
      while (flag) {
        flag = false;
        for (int i = 0; i < m; ++i) {
          if (Scalars.lessThan(scalars[i + 1], scalars[i])) {
            flag = true;
            ++swaps;
            Scalar copy = scalars[i];
            scalars[i] = scalars[i + 1];
            scalars[i + 1] = copy;
          }
        }
        --m;
      }
      Deque<Scalar> deque = new ArrayDeque<>();
      Stream.of(scalars).forEach(scalar -> {
        if (!deque.isEmpty() && deque.peekLast().equals(scalar))
          deque.pollLast();
        else
          deque.add(scalar);
      });
      sign = SIGN[swaps % 2];
      normal = Tensor.of(deque.stream());
    }

    public Scalar sign() {
      return sign;
    }
  }
}
