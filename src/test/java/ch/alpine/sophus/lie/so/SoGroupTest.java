// code by jph
package ch.alpine.sophus.lie.so;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;

public class SoGroupTest {
  @Test
  public void testDetNegFail() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.element(DiagonalMatrix.of(1, 1, -1)));
  }

  @Test
  public void testFormat4Ok() {
    SoGroup.INSTANCE.element(IdentityMatrix.of(4));
  }

  @Test
  public void testNullFail() {
    assertThrows(Exception.class, () -> SoGroup.INSTANCE.element(null));
  }
}
