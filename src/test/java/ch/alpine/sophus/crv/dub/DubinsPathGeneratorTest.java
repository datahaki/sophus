// code by jph
package ch.alpine.sophus.crv.dub;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Subdivide;
import ch.alpine.tensor.api.ScalarTensorFunction;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

class DubinsPathGeneratorTest {
  @Test
  public void testSimple() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LSR, Quantity.of(1, "m"), Tensors.fromString("{" + Math.PI / 2 + "[m], 10[m], " + Math.PI / 2 + "[m]}"));
    Tensor g0 = Tensors.fromString("{0[m], 0[m], 0}").unmodifiable();
    ScalarTensorFunction scalarTensorFunction = dubinsPath.sampler(g0);
    Chop._12.requireClose(scalarTensorFunction.apply(Quantity.of(0, "m")), g0);
    {
      Tensor tensor = scalarTensorFunction.apply(Quantity.of(1.5707963267948966, "m"));
      Chop._12.requireClose(tensor, Tensors.fromString("{1[m], 1[m], 1.5707963267948966}"));
    }
    {
      Tensor tensor = scalarTensorFunction.apply(Quantity.of(11.570796326794897, "m"));
      Chop._12.requireClose(tensor, Tensors.fromString("{1[m], 11[m], 1.5707963267948966}"));
    }
    {
      Tensor tensor = scalarTensorFunction.apply(Quantity.of(13.141592653589793, "m"));
      Chop._12.requireClose(tensor, Tensors.fromString("{2[m], 12[m], 0}"));
    }
  }

  @Test
  public void testZeroLength() {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LSR, Quantity.of(1, "m"), Tensors.fromString("{0[m], 0[m], 0[m]}"));
    Tensor g0 = Tensors.fromString("{1[m], 2[m], 3}").unmodifiable();
    assertTrue(Scalars.isZero(dubinsPath.length()));
    Subdivide.of(0, 1, 10).map(dubinsPath.sampler(g0));
    Subdivide.of(0, 1, 10).map(dubinsPath.unit(g0));
  }

  @Test
  public void testFail() throws ClassNotFoundException, IOException {
    DubinsPath dubinsPath = DubinsPath.of(DubinsType.LSR, Quantity.of(1, "m"), Tensors.fromString("{" + Math.PI / 2 + "[m], 10[m], " + Math.PI / 2 + "[m]}"));
    Tensor g0 = Tensors.fromString("{0[m], 0[m], 0}").unmodifiable();
    ScalarTensorFunction scalarTensorFunction = Serialization.copy(dubinsPath.sampler(g0));
    assertThrows(Exception.class, () -> scalarTensorFunction.apply(Quantity.of(-0.1, "m")));
    Scalar exceed = Quantity.of(0.1, "m").add(dubinsPath.length());
    assertThrows(Exception.class, () -> scalarTensorFunction.apply(exceed));
  }
}
