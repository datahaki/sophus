// code by jph
package ch.alpine.sophus.lie.so;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;

public class SoGroupTest {
  @Test
  public void testDetNegFail() {
    AssertFail.of(() -> SoGroup.INSTANCE.element(DiagonalMatrix.of(1, 1, -1)));
  }

  @Test
  public void testFormat4Ok() {
    SoGroup.INSTANCE.element(IdentityMatrix.of(4));
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> SoGroup.INSTANCE.element(null));
  }
}
