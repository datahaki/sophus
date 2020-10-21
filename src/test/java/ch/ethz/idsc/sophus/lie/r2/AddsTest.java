// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.RotateLeft;
import ch.ethz.idsc.tensor.pdf.DiscreteUniformDistribution;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
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
