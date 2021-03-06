// code by jph
package ch.ethz.idsc.sophus.clt.mid;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class MidpointTangentLayer1Test extends TestCase {
  public void testMathematicaSync() {
    Scalar scalar = MidpointTangentLayer1.INSTANCE.apply(RealScalar.of(1), RealScalar.of(3));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(5.775145853064506));
  }
}
