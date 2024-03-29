// code by jph
package ch.alpine.sophus.crv.se2c;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.sca.Chop;

class LogarithmicSpiralTest {
  @Test
  void testSimple() {
    ScalarTensorFunction scalarTensorFunction = LogarithmicSpiral.of(2, 0.1759);
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ZERO), //
        Tensors.vector(2, 0, 1.3966775374758775));
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ONE), //
        Tensors.vector(1.2884252164237864, 2.0066033846985687, 2.3966775374758775));
  }

  @Test
  void testScalars() {
    ScalarTensorFunction scalarTensorFunction = LogarithmicSpiral.of(RealScalar.of(2), RealScalar.of(0.1759));
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ZERO), //
        Tensors.vector(2, 0, 1.3966775374758775));
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ONE), //
        Tensors.vector(1.2884252164237864, 2.0066033846985687, 2.3966775374758775));
  }

  @Test
  void testNullFail() {
    assertThrows(Exception.class, () -> LogarithmicSpiral.of(RealScalar.of(2), null));
    assertThrows(Exception.class, () -> LogarithmicSpiral.of(null, RealScalar.of(2)));
  }
}
