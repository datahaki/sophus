// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.RotateLeft;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.d.DiscreteUniformDistribution;
import junit.framework.TestCase;

public class AddsTest extends TestCase {
  public void testSimple() {
    assertEquals(Adds.forward(Tensors.vector(1, 2, 3)), Tensors.vector(3, 5, 4));
    assertEquals(Adds.reverse(Tensors.vector(1, 2, 3)), Tensors.vector(4, 3, 5));
  }

  public void testRandom() {
    Distribution distribution = DiscreteUniformDistribution.of(-10, 10);
    for (int n = 1; n < 10; ++n) {
      Tensor tensor = RandomVariate.of(distribution, n);
      assertEquals(RotateLeft.of(Adds.forward(tensor), -1), Adds.reverse(tensor));
    }
  }

  public void testNegFail() {
    AssertFail.of(() -> Adds.matrix(-1));
  }
}
