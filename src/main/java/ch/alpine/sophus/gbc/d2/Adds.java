// code by jph
package ch.alpine.sophus.gbc.d2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.Unprotect;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.num.Boole;

/* package */ enum Adds {
  ;
  /** @param tensor non-empty
   * @return */
  public static Tensor forward(Tensor tensor) {
    List<Tensor> list = new ArrayList<>(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor prev = iterator.next();
    Tensor _1st = prev;
    while (iterator.hasNext())
      list.add(prev.add(prev = iterator.next()));
    list.add(prev.add(_1st));
    Integers.requireEquals(tensor.length(), list.size());
    return Unprotect.using(list);
  }

  /** @param tensor non-empty
   * @return */
  public static Tensor reverse(Tensor tensor) {
    List<Tensor> list = new ArrayList<>(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    for (Tensor prev = Last.of(tensor); iterator.hasNext();)
      list.add(prev.add(prev = iterator.next()));
    Integers.requireEquals(tensor.length(), list.size());
    return Unprotect.using(list);
  }

  // ---
  private static final int CACHE_SIZE = 32;
  private static final Function<Integer, Tensor> CACHE = Cache.of(Adds::build, CACHE_SIZE);

  /** @param n strictly positive
   * @return */
  public static Tensor matrix(int n) {
    return CACHE.apply(Integers.requirePositive(n));
  }

  private static Tensor build(int n) {
    // TODO SOPHUS ALG implement as sparse matrix
    return Tensors.matrix((i, j) -> Boole.of(Math.floorMod(j - i, n) < 2), n, n);
  }
}
