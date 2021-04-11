// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class StRandomSampleTest extends TestCase {
  public void testFail() {
    AssertFail.of(() -> StRandomSample.of(3, -1));
    AssertFail.of(() -> StRandomSample.of(3, 4));
    AssertFail.of(() -> StRandomSample.of(-3, -4));
  }
}
