// code by jph
package ch.ethz.idsc.sophus.ply.d2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class ThreePointHomogeneousTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> ThreePointWeighting.of(null));
  }
}
