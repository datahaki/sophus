// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;
import junit.framework.TestCase;

public class SphericalVariogramTest extends TestCase {
  public void testSimple() {
    ScalarUnaryOperator variogram = SphericalVariogram.of(5, 3);
    Scalar lo = variogram.apply(RealScalar.of(4.9999));
    Scalar hi = variogram.apply(RealScalar.of(5.0001));
    Chop._05.requireClose(lo, hi);
    Chop._05.requireClose(lo, RealScalar.of(3));
  }
}
