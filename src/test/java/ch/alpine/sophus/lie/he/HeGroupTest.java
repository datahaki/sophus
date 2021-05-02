// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class HeGroupTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> HeGroup.INSTANCE.element(null));
  }
}
