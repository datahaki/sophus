// code by jph
package ch.alpine.sophus.hs.sn;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.r2.CirclePoints;

public class SnMemberQTest {
  @Test
  public void testD1() {
    SnMemberQ.INSTANCE.require(Tensors.vector(+1));
    SnMemberQ.INSTANCE.require(Tensors.vector(-1));
  }

  @Test
  public void testD2() {
    for (int n = 5; n < 14; ++n)
      CirclePoints.of(n).stream().forEach(SnMemberQ.INSTANCE::require);
  }

  @Test
  public void testNullFail() {
    AssertFail.of(() -> SnMemberQ.INSTANCE.test(null));
  }
}
