// code by jph
package ch.ethz.idsc.sophus.clt.mid;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class MidpointTangentLayer2Test extends TestCase {
  public void testMathematicaSync() {
    Scalar scalar = MidpointTangentLayer2.INSTANCE.apply(RealScalar.of(1), RealScalar.of(6));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(-6.584681341407497));
  }
}
