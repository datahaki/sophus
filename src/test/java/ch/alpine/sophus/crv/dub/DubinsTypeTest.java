package ch.alpine.sophus.crv.dub;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DubinsTypeTest {
  @Test
  void test() {
    assertEquals(DubinsType.values().length, 6);
  }
}
