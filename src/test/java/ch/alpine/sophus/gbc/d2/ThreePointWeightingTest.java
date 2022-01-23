// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class ThreePointWeightingTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> new ThreePointWeighting(null));
  }
}
