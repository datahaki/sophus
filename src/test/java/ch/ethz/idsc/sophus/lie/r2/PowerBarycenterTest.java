// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.IOException;
import java.util.function.BiFunction;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class PowerBarycenterTest extends TestCase {
  public void testSerialization() throws ClassNotFoundException, IOException {
    BiFunction<Tensor, Scalar, Tensor> biFunction = Serialization.copy(PowerBarycenter.of(1.5));
    Tensor tensor = biFunction.apply(Tensors.vector(1, 2, 3), RealScalar.of(0.3));
    Chop._10.requireClose(tensor, Tensors.vector(6.0858061945018465, 12.171612389003693, 18.25741858350554));
  }

  public void testExpFail() {
    PowerBarycenter.of(0.0);
    PowerBarycenter.of(2.0);
    try {
      PowerBarycenter.of(-0.1);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      PowerBarycenter.of(2.1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
