// code by jph
package ch.alpine.sophus.crv.clt.mid;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;

class MidpointTangentLayer2Test {
  @Test
  void testMathematicaSync() {
    Scalar scalar = MidpointTangentLayer2.INSTANCE.apply(RealScalar.of(1), RealScalar.of(6));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(-6.584681341407497));
  }
}
