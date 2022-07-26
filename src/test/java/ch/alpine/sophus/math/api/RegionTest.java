// code by jph
package ch.alpine.sophus.math.api;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class RegionTest {
  @FunctionalInterface
  public interface Some {
    boolean single(Object s);
  }

  @Test
  void testSimple() {
    Some some = s -> false;
    Optional<Integer> optional = Stream.of(3).filter(some::single).findAny();
    assertFalse(optional.isPresent());
  }
}
