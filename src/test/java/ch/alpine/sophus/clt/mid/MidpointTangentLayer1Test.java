// code by jph
package ch.alpine.sophus.clt.mid;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;

class MidpointTangentLayer1Test {
  @Test
  public void testMathematicaSync() {
    Scalar scalar = MidpointTangentLayer1.INSTANCE.apply(RealScalar.of(1), RealScalar.of(3));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(5.775145853064506));
  }
}
