// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class ExponentialVariogramTest extends TestCase {
  public void testSimple() {
    ScalarUnaryOperator scalarUnaryOperator = ExponentialVariogram.of(Quantity.of(1, "m"), RealScalar.of(2));
    Scalar value = scalarUnaryOperator.apply(RnNorm.INSTANCE.norm(Tensors.fromString("{2[m], 3[m]}")));
    Tolerance.CHOP.requireClose(value, RealScalar.of(1.9456550776555288));
  }
}
