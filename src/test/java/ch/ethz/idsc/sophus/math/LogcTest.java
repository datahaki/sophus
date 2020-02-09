// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LogcTest extends TestCase {
  public void testSimple() {
    Scalar scalar = Logc.FUNCTION.apply(RealScalar.of(1 + 1e-13));
    Chop._08.requireClose(scalar, RealScalar.ONE);
  }
}
