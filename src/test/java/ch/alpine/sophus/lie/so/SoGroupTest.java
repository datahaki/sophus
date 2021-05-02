// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class SoGroupTest extends TestCase {
  public void testDetNegFail() {
    AssertFail.of(() -> SoGroup.INSTANCE.element(DiagonalMatrix.of(1, 1, -1)));
  }

  public void testFormat4Ok() {
    SoGroup.INSTANCE.element(IdentityMatrix.of(4));
  }

  public void testNullFail() {
    AssertFail.of(() -> SoGroup.INSTANCE.element(null));
  }
}
