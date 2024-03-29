// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

class Se3NumericsTest {
  @Test
  void testSimple() {
    Se3Numerics se3Numerics1 = new Se3Numerics(RealScalar.of(1e-2));
    Se3Numerics se3Numerics2 = new Se3Numerics(RealScalar.of(0.9e-2));
    Tensor diff = se3Numerics1.vector().subtract(se3Numerics2.vector());
    assertTrue(Scalars.nonZero(diff.Get(0)));
    Chop._05.requireAllZero(diff.extract(1, 5));
  }
}
