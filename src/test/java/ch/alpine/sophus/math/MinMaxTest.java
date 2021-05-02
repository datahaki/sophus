// code by jph
package ch.alpine.sophus.math;

import java.io.IOException;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class MinMaxTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    Tensor tensor = Tensors.fromString("{{1, 9, 3}, {4, 5, -6}}");
    MinMax minMax = Serialization.copy(MinMax.of(tensor));
    assertEquals(minMax.min(), Tensors.vector(1, 5, -6));
    assertEquals(minMax.max(), Tensors.vector(4, 9, 3));
  }

  public void testFail() {
    Tensor tensor = Tensors.fromString("{{1, 9, 3}, {4, 5}}");
    AssertFail.of(() -> MinMax.of(tensor));
  }

  public void testFailScalar() {
    AssertFail.of(() -> MinMax.of(RealScalar.ZERO));
  }
}
