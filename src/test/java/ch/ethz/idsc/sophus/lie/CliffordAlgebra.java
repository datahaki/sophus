// code by jph
package ch.ethz.idsc.sophus.lie;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.Unprotect;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.alg.Join;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.alg.Subsets;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.ext.Integers;
import ch.ethz.idsc.tensor.io.Pretty;
import ch.ethz.idsc.tensor.io.ScalarArray;

public enum CliffordAlgebra {
  ;
  private static final Scalar[] SIGN = { RealScalar.ONE, RealScalar.ONE.negate() };
  private static final int MAX_SIZE = 4;
  private static final Function<Integer, Tensor> CACHE = Cache.of(CliffordAlgebra::build, MAX_SIZE);

  /** @param n
   * @return */
  public static Tensor of(int n) {
    return CACHE.apply(Integers.requirePositiveOrZero(n));
  }

  private static Tensor build(int n) {
    Tensor range = Range.of(0, n);
    int m = 1 << n;
    List<Tensor> list = new ArrayList<>(m);
    Map<Tensor, Integer> map = new HashMap<>();
    for (int k = 0; k <= n; ++k)
      for (Tensor perm : Subsets.of(range, k)) {
        list.add(perm);
        map.put(perm, map.size());
      }
    Tensor tensor = Array.zeros(m, m, m);
    for (int i = 0; i < m; ++i)
      for (int j = 0; j < m; ++j) {
        Tensor c = Join.of(list.get(i), list.get(j));
        Tensor reduce = reduce(c);
        Scalar sign = reduce.Get(0);
        Tensor key = reduce.get(1);
        int k = map.get(key);
        tensor.set(sign, k, j, i);
      }
    return tensor;
  }

  private static Tensor reduce(Tensor a) {
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
    return Unprotect.byRef(SIGN[swaps % 2], Tensor.of(deque.stream()));
  }

  public static void main(String[] args) {
    Tensor ad = CliffordAlgebra.of(2);
    Tensor tensor = ad.dot(Tensors.vector(0, 0, 0, 1));
    System.out.println(Pretty.of(tensor));
    // JacobiIdentity.require(ad);
    // System.out.println(ad);
    // Tensor reduce = reduce(Tensors.vector(1, 3));
    // System.out.println(reduce);
  }
}
