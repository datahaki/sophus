// code by jph
package ch.ethz.idsc.sophus.math.var;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class GaussianVariogramTest extends TestCase {
  public void testQuantity() {
    ScalarUnaryOperator variogram = GaussianVariogram.of(Quantity.of(2, "m"));
    Scalar lo = variogram.apply(Quantity.of(1, "m"));
    Chop._05.requireClose(lo, RealScalar.of(0.7788007830714049));
    Scalar hi = variogram.apply(Quantity.of(5, "m"));
    Chop._05.requireClose(hi, RealScalar.of(0.0019304541362277093));
  }
}
