// code by jph
package ch.alpine.sophus.gbc.d2;

import java.util.Iterator;
import java.util.function.Function;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.ext.Cache;

/* package */ enum Adds {
  ;
  /** @param tensor non-empty
   * @return */
  public static Tensor forward(Tensor tensor) {
    Tensor result = Tensors.reserve(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    Tensor prev = iterator.next();
    Tensor _1st = prev;
    while (iterator.hasNext())
      result.append(prev.add(prev = iterator.next()));
    return result.append(prev.add(_1st));
  }

  /** @param tensor non-empty
   * @return */
  public static Tensor reverse(Tensor tensor) {
    Tensor result = Tensors.reserve(tensor.length());
    Iterator<Tensor> iterator = tensor.iterator();
    for (Tensor prev = Last.of(tensor); iterator.hasNext();)
      result.append(prev.add(prev = iterator.next()));
    return result;
  }

  // ---
  private static final int CACHE_SIZE = 32;
  private static final Function<Integer, Tensor> CACHE = Cache.of(Adds::build, CACHE_SIZE);

  /** @param n
   * @return */
  public static Tensor matrix(int n) {
    return CACHE.apply(n);
  }

  private static Tensor build(int n) {
    return Array.of(list -> Math.floorMod(list.get(1) - list.get(0), n) < 2 //
        ? RealScalar.ONE
        : RealScalar.ZERO, n, n);
  }
}
