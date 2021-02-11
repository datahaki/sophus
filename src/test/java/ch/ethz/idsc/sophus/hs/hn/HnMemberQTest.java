// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.alg.UnitVector;
import junit.framework.TestCase;

public class HnMemberQTest extends TestCase {
  public void testSimple() {
    for (int n = 2; n < 10; ++n)
      HnMemberQ.INSTANCE.require(UnitVector.of(n, n - 1));
    assertFalse(HnMemberQ.INSTANCE.test(UnitVector.of(4, 1)));
  }
}
