// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.alg.UnitVector;
import junit.framework.TestCase;

public class HnMemberQTest extends TestCase {
  public void testSimple() {
    HnMemberQ.INSTANCE.require(UnitVector.of(4, 3));
    assertFalse(HnMemberQ.INSTANCE.test(UnitVector.of(4, 1)));
  }

  public void testNullFail() {
    AssertFail.of(() -> new THnMemberQ(null));
  }
}
