// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.Unit;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testSimple() {
    Unit unit = StaticHelper.unique(IdentityMatrix.of(3, Quantity.of(1, "s^2*m^-1")));
    assertEquals(unit, Unit.of("s^2*m^-1"));
  }
}
