// code by jph
package ch.alpine.sophus.lie.se;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieAlgebraAds;

class SeAlgebraTest {
  @Test
  void testSimple() {
    assertEquals(new SeNGroup(4).matrixBasis().length(), 4 + 6);
  }

  @Test
  void testNFail() {
    assertThrows(Exception.class, () -> LieAlgebraAds.se(0));
    assertThrows(Exception.class, () -> LieAlgebraAds.se(-1));
  }
}
