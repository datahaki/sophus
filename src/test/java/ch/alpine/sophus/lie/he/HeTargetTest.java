// code by jph
package ch.alpine.sophus.lie.he;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.nrm.Vector2Norm;

public class HeTargetTest {
  @Test
  public void testFailNegative() {
    AssertFail.of(() -> new HeTarget(Vector2Norm::of, RealScalar.of(-1)));
  }
}
