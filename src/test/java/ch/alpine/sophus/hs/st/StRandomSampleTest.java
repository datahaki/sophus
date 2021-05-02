// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class StRandomSampleTest extends TestCase {
  public void testFail() {
    AssertFail.of(() -> StRandomSample.of(3, -1));
    AssertFail.of(() -> StRandomSample.of(3, 4));
    AssertFail.of(() -> StRandomSample.of(-3, -4));
  }
}
