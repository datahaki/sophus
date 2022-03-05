// code by jph
package ch.alpine.sophus.api;

import java.util.Optional;
import java.util.stream.Stream;

import junit.framework.TestCase;

public class RegionTest extends TestCase {
  @FunctionalInterface
  public static interface Some {
    public boolean single(Object s);
  }

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
