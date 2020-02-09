// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class So3BiinvariantMeanEquationTest extends TestCase {
  public void testSimple() {
    Tensor sequence = Tensors.of( //
        So3Exponential.INSTANCE.exp(Tensors.vector(+1 + 0.3, 0, 0)), //
        So3Exponential.INSTANCE.exp(Tensors.vector(+0 + 0.3, 0, 0)), //
        So3Exponential.INSTANCE.exp(Tensors.vector(-1 + 0.3, 0, 0)));
    Tensor log = So3BiinvariantMeanEquation.INSTANCE.evaluate( //
        sequence, Tensors.vector(0.25, 0.5, 0.25), So3Exponential.INSTANCE.exp(Tensors.vector(+0.3, 0, 0)));
    Chop._10.requireAllZero(log);
  }
}
