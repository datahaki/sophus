// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class LeversWeightingTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> new LeversWeighting(null));
  }
}
