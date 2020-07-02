// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.io.IOException;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class ThinPlateSplineVariogramTest extends TestCase {
  public void testQuantity() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator scalarUnaryOperator = Serialization.copy(ThinPlateSplineVariogram.of(Quantity.of(1, "m")));
    Tolerance.CHOP.requireClose(scalarUnaryOperator.apply(Quantity.of(0.2, "m")), Scalars.fromString("-0.06437751649736402[m^2]"));
    Tolerance.CHOP.requireClose(scalarUnaryOperator.apply(Quantity.of(0.0, "m")), Scalars.fromString("0[m^2]"));
  }

  public void testFailNonPositive() {
    try {
      ThinPlateSplineVariogram.of(RealScalar.ZERO);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
