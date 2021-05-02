// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.nrm.Vector2Norm;
import junit.framework.TestCase;

public class HeTargetTest extends TestCase {
  public void testFailNegative() {
    AssertFail.of(() -> new HeTarget(Vector2Norm::of, RealScalar.of(-1)));
  }
}
