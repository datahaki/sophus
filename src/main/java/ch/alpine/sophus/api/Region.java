// code by jph
package ch.alpine.sophus.api;

import java.util.function.Predicate;

import ch.alpine.tensor.Tensor;

/** determines membership for elements of type T
 * 
 * common examples for type T are {@link Tensor} and StateTime
 * 
 * membership status of given element
 * 
 * @see MemberQ */
@FunctionalInterface
public interface Region<T> extends Predicate<T> {
  // ---
}
