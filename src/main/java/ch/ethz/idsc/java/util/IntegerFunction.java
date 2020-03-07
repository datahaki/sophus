// code by jph
package ch.ethz.idsc.java.util;

import java.io.Serializable;
import java.util.function.Function;

/** serializable function that maps an integer to a tensor
 * 
 * interface can be used to conveniently cast a function to a serializable function.
 * preferably, the interface should not be used as type inside a class. */
@FunctionalInterface
public interface IntegerFunction<T> extends Function<Integer, T>, Serializable {
  // ---
}
