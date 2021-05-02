// code by jph
package ch.alpine.sophus.math.var;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.qty.Quantity;
import junit.framework.TestCase;

public class ExponentialVariogramTest extends TestCase {
  public void testSimple() {
    ScalarUnaryOperator scalarUnaryOperator = ExponentialVariogram.of(Quantity.of(1, "m"), RealScalar.of(2));
    Scalar value = scalarUnaryOperator.apply(Vector2Norm.of(Tensors.fromString("{2[m], 3[m]}")));
    Tolerance.CHOP.requireClose(value, RealScalar.of(1.9456550776555288));
  }
}
