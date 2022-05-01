// code by jph
package ch.alpine.sophus.api;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;

class RegionTest {
  @FunctionalInterface
  public static interface Some {
    public boolean single(Object s);
  }

  @Test
  public void testSimple() {
    Some some = new Some() {
      @Override
      public boolean single(Object s) {
        return false;
      }
    };
    Optional<Integer> optional = Stream.of(3).filter(some::single).findAny();
    assertFalse(optional.isPresent());
  }
}
