// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class RnGroupTest extends TestCase {
  public void testFailNull() {
    AssertFail.of(() -> RnGroup.INSTANCE.element(null));
  }
  // public void testFailMatrix() {
  // try {
  // RnGroup.INSTANCE.element(IdentityMatrix.of(3));
  // fail();
  // } catch (Exception exception) {
  // // ---
  // }
  // }
}
