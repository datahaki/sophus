// code by jph
package ch.alpine.sophus.math;

import java.util.function.Supplier;

import ch.alpine.tensor.ext.Integers;

/** used to perform n-number of iterations
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/Do.html">Do</a> */
public enum Do {
  ;
  /** @param supplier
   * @param n */
  public static <T> T of(Supplier<T> supplier, final int n) {
    Integers.requirePositive(n);
    T value = null;
    for (int index = 0; index < n; ++index)
      value = supplier.get();
    return value;
  }

  /** @param fallback
   * @param supplier
   * @param n non-negative
   * @return fallback if n == 0, otherwise return value of suppliers after n invocations */
  public static <T> T of(T fallback, Supplier<T> supplier, final int n) {
    return n == 0 //
        ? fallback
        : of(supplier, n);
  }
}
