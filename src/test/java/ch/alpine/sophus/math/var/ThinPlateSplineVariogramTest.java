// code by jph
package ch.alpine.sophus.math.var;

import java.io.IOException;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class ThinPlateSplineVariogramTest extends TestCase {
  public void testSimple() {
    ScalarUnaryOperator scalarUnaryOperator = ThinPlateSplineVariogram.of(3);
    Scalar scalar = scalarUnaryOperator.apply(RealScalar.of(2));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(-1.621860432432657));
  }

  public void testQuantity() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator scalarUnaryOperator = Serialization.copy(ThinPlateSplineVariogram.of(Quantity.of(1, "m")));
    Tolerance.CHOP.requireClose(scalarUnaryOperator.apply(Quantity.of(0.2, "m")), Scalars.fromString("-0.06437751649736402[m^2]"));
    Tolerance.CHOP.requireClose(scalarUnaryOperator.apply(Quantity.of(0.0, "m")), Scalars.fromString("0[m^2]"));
  }

  public void testFailNonPositive() {
    AssertFail.of(() -> ThinPlateSplineVariogram.of(RealScalar.ZERO));
  }
}