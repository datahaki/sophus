// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class ThreePointHomogeneousTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> ThreePointWeighting.of(null));
  }
}
