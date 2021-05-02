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

public class MultiquadricVariogramTest extends TestCase {
  public void testQuantity() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator scalarUnaryOperator = Serialization.copy(MultiquadricVariogram.of(Quantity.of(1, "m")));
    Scalar scalar = scalarUnaryOperator.apply(Quantity.of(0.2, "m"));
    Tolerance.CHOP.requireClose(scalar, Scalars.fromString("1.019803902718557[m]"));
  }

  public void testQuantityZero() {
    ScalarUnaryOperator scalarUnaryOperator = MultiquadricVariogram.of(Quantity.of(0, "m"));
    Scalar scalar = scalarUnaryOperator.apply(Quantity.of(0.2, "m"));
    Tolerance.CHOP.requireClose(scalar, Scalars.fromString("0.2[m]"));
  }

  public void testFailNonPositive() {
    AssertFail.of(() -> MultiquadricVariogram.of(RealScalar.of(-1)));
  }
}
