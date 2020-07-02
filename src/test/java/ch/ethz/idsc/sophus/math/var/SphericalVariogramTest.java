// code by jph
package ch.ethz.idsc.sophus.math.var;

import java.io.IOException;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class SphericalVariogramTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    ScalarUnaryOperator variogram = Serialization.copy(SphericalVariogram.of(5, 3));
    Scalar lo = variogram.apply(RealScalar.of(4.9999));
    Scalar hi = variogram.apply(RealScalar.of(5.0001));
    Chop._05.requireClose(lo, hi);
    Chop._05.requireClose(lo, RealScalar.of(3));
  }

  public void testQuantity() {
    ScalarUnaryOperator variogram = SphericalVariogram.of(Quantity.of(2, "m"), Quantity.of(4, "s"));
    Scalar lo = variogram.apply(Quantity.of(1, "m"));
    Scalar hi = variogram.apply(Quantity.of(5, "m"));
    Chop._05.requireClose(lo, Scalars.fromString("11/4[s]"));
    Chop._05.requireClose(hi, Quantity.of(4, "s"));
  }
}
