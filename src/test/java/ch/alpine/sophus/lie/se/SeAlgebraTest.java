// code by jph
package ch.alpine.sophus.lie.se;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.LieAlgebraAds;
import ch.alpine.sophus.lie.LieAlgebraMatrixBasis;
import ch.alpine.tensor.Tensor;

class SeAlgebraTest {
  @Test
  void testSimple() {
    SeNGroup seNGroup = new SeNGroup(4);
    Tensor basis = LieAlgebraMatrixBasis.of(seNGroup);
    assertEquals(basis.length(), 4 + 6);
  }

  @Test
  void testNFail() {
    assertThrows(Exception.class, () -> LieAlgebraAds.se(0));
    assertThrows(Exception.class, () -> LieAlgebraAds.se(-1));
  }
}
