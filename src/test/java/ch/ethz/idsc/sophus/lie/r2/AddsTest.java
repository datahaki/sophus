// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class AddsTest extends TestCase {
  public void testSimple() {
    Tensor tensor = Adds.of(Tensors.vector(1, 2, 3));
    assertEquals(tensor, Tensors.vector(3, 5, 4));
  }

  public void testNegFail() {
    AssertFail.of(() -> Adds.matrix(-1));
  }
}
