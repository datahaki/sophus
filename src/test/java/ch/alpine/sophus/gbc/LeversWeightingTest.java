// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.itp.InverseDistanceWeighting;
import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class LeversWeightingTest extends TestCase {
  public void testSimple() {
    AssertFail.of(() -> InverseDistanceWeighting.of(null));
  }
}
