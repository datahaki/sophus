// code by jph
package ch.alpine.sophus.lie.rn;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;

public class RnGroupTest {
  @Test
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
