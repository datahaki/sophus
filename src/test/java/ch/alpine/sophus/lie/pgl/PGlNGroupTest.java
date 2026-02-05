package ch.alpine.sophus.lie.pgl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PGlNGroupTest {
  @Test
  void test() {
    PGlNGroup pGlNGroup = new PGlNGroup(3);
    assertEquals(pGlNGroup.dimensions(), 8);
  }
}
