// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.StringScalarQ;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.qty.Unit;
import ch.ethz.idsc.tensor.qty.Units;
import junit.framework.TestCase;

public class StaticHelperTest extends TestCase {
  public void testSimple() {
    Unit unit = StaticHelper.uniqueUnit(DiagonalMatrix.of(3, Quantity.of(1, "s^2*m^-1")));
    assertEquals(unit, Unit.of("s^2*m^-1"));
  }

  public void testOne() {
    Tensor tensor = Tensors.vector(2, 3, 4);
    Unit unit = StaticHelper.uniqueUnit(tensor);
    assertTrue(Units.isOne(unit));
  }

  public void testFail() {
    Tensor tensor = Tensors.fromString("{1[m], 2[s]}");
    assertFalse(StringScalarQ.any(tensor));
    try {
      StaticHelper.uniqueUnit(tensor);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
