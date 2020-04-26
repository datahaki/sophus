// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.IOException;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
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
    try {
      MultiquadricVariogram.of(RealScalar.of(-1));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
