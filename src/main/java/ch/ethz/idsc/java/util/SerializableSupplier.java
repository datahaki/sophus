// code by jph
package ch.ethz.idsc.java.util;

import java.io.Serializable;
import java.util.function.Supplier;

@FunctionalInterface
public interface SerializableSupplier<T> extends Supplier<T>, Serializable {
  // ---
}