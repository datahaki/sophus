// code by jph
package ch.alpine.sophus.math;

import java.util.function.Predicate;

import ch.alpine.tensor.Tensor;

/** determines membership for elements of type T
 * 
 * common examples for type T are {@link Tensor} and StateTime
 * 
 * membership status of given element */
@FunctionalInterface
public interface Region<T> extends Predicate<T> {
  // ---
}
