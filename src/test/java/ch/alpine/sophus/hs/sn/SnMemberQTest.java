// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.r2.CirclePoints;
import junit.framework.TestCase;

public class SnMemberQTest extends TestCase {
  public void testD1() {
    SnMemberQ.INSTANCE.require(Tensors.vector(+1));
    SnMemberQ.INSTANCE.require(Tensors.vector(-1));
  }

  public void testD2() {
    for (int n = 5; n < 14; ++n)
      CirclePoints.of(n).stream().forEach(SnMemberQ.INSTANCE::require);
  }

  public void testNullFail() {
    AssertFail.of(() -> SnMemberQ.INSTANCE.test(null));
  }
}
