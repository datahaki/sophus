// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.tensor.ExactTensorQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class HeGeodesicTest extends TestCase {
  public void testSimple() {
    Tensor p = Tensors.fromString("{{1, 2, 3}, {4, 5, 6}, 7}");
    Tensor q = Tensors.fromString("{{-1, 6, 2}, {-3, -2, 1}, -4}");
    Tensor actual = HeGeodesic.INSTANCE.split(p, q, RationalScalar.HALF);
    ExactTensorQ.require(actual);
    Tensor expect = Tensors.fromString("{{0, 4, 5/2}, {1/2, 3/2, 7/2}, 21/8}");
    assertEquals(actual, expect);
  }
}
