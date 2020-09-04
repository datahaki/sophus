// code by jph
package ch.ethz.idsc.tensor.num;

import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class PermutationPowerTest extends TestCase {
  public void testSimple() {
    assertEquals( //
        PermutationPower.of(Cycles.of(Tensors.fromString("{{4, 2, 5}, {6, 3, 1, 7}}")), 6), //
        Cycles.of(Tensors.fromString("{{1, 6}, {3, 7}}")));
    assertEquals( //
        PermutationPower.of(Cycles.of(Tensors.fromString("{{4, 2, 5}, {6, 3, 1, 7}}")), -2), //
        Cycles.of(Tensors.fromString("{{1, 6}, {2, 5, 4}, {3, 7}}")));
    assertEquals( //
        PermutationPower.of(Cycles.of(Tensors.fromString("{{4, 2, 5}, {6, 3, 1, 7}}")), 12), //
        Cycles.identity());
  }
}
