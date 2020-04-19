// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class CyclesTest extends TestCase {
  public void testSimple() {
    Cycles cycles = Cycles.of(Tensors.fromString("{{5, 9}, {7, 14, 13}, {18, 4, 10, 19, 6}, {20, 1}, {}}"));
    Tensor tensor = cycles.toCoordinate();
    assertEquals(tensor, Tensors.fromString("{{1, 20}, {4, 10, 19, 6, 18}, {5, 9}, {7, 14, 13}}"));
  }
}
