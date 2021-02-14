// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;
import junit.framework.TestCase;

public class HeTargetTest extends TestCase {
  public void testFailNegative() {
    AssertFail.of(() -> new HeTarget(VectorNorm2::of, RealScalar.of(-1)));
  }
}
