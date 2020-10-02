// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class HeGroupTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> HeGroup.INSTANCE.element(null));
  }
}
