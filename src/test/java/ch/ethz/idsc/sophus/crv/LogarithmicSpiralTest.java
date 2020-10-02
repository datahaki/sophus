// code by jph
package ch.ethz.idsc.sophus.crv;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.opt.ScalarTensorFunction;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LogarithmicSpiralTest extends TestCase {
  public void testSimple() {
    ScalarTensorFunction scalarTensorFunction = LogarithmicSpiral.of(2, 0.1759);
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ZERO), //
        Tensors.vector(2, 0, 1.3966775374758775));
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ONE), //
        Tensors.vector(1.2884252164237864, 2.0066033846985687, 2.3966775374758775));
  }

  public void testScalars() {
    ScalarTensorFunction scalarTensorFunction = LogarithmicSpiral.of(RealScalar.of(2), RealScalar.of(0.1759));
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ZERO), //
        Tensors.vector(2, 0, 1.3966775374758775));
    Chop._12.requireClose(scalarTensorFunction.apply(RealScalar.ONE), //
        Tensors.vector(1.2884252164237864, 2.0066033846985687, 2.3966775374758775));
  }

  public void testNullFail() {
    AssertFail.of(() -> LogarithmicSpiral.of(RealScalar.of(2), null));
    AssertFail.of(() -> LogarithmicSpiral.of(null, RealScalar.of(2)));
  }
}
