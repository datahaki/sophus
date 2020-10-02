// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class SonGroupTest extends TestCase {
  public void testDetNegFail() {
    AssertFail.of(() -> SonGroup.INSTANCE.element(DiagonalMatrix.of(1, 1, -1)));
  }

  public void testFormat4Ok() {
    SonGroup.INSTANCE.element(IdentityMatrix.of(4));
  }

  public void testNullFail() {
    AssertFail.of(() -> SonGroup.INSTANCE.element(null));
  }
}
