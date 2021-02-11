// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class THnMemberQTest extends TestCase {
  public void testNullFail() {
    AssertFail.of(() -> new THnMemberQ(null));
  }
}
