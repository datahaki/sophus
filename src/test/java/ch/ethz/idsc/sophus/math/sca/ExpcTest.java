// code by jph
package ch.ethz.idsc.sophus.math.sca;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class ExpcTest extends TestCase {
  public void testSimple() {
    Scalar scalar = Expc.FUNCTION.apply(RealScalar.of(-1e-13));
    Chop._02.requireClose(scalar, RealScalar.ONE);
  }
}
