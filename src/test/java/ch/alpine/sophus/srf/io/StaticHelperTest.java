// code by jph
package ch.alpine.sophus.srf.io;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StaticHelperTest {
  @Test
  public void testSingle() {
    String[] strings = StaticHelper.slash("12//");
    assertEquals(strings[0], "12");
    assertEquals(strings[1], "");
    assertEquals(strings[2], "");
  }

  @Test
  public void testDual() {
    String[] strings = StaticHelper.slash("12//34");
    assertEquals(strings[0], "12");
    assertEquals(strings[1], "");
    assertEquals(strings[2], "34");
  }
}