// code by jph
package ch.alpine.sophus.lie.he;

import ch.alpine.tensor.ExactTensorQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
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