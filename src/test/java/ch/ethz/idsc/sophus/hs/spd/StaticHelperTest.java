// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testSimple() {
    assertEquals(StaticHelper.norm(IdentityMatrix.of(3)), RealScalar.ZERO);
  }
}
