// code by jph
package ch.alpine.sophus.ply.d2;

import java.io.IOException;
import java.util.function.BiFunction;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.sca.Chop;
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
    AssertFail.of(() -> PowerBarycenter.of(-0.1));
    AssertFail.of(() -> PowerBarycenter.of(2.1));
  }
}
