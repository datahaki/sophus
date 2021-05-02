// code by jph
package ch.alpine.sophus.ref.d1h;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensors;
import junit.framework.TestCase;

public class RnHermite1SubdivisionsTest extends TestCase {
  public void testSimple() {
    RnHermite1Subdivision hs = RnHermite1Subdivisions.of(RationalScalar.of(-1, 8), RationalScalar.of(-1, 2));
    assertEquals(hs.AMP, Tensors.fromString("{{1/2, +1/8}, {-3/4, -1/8}}"));
    assertEquals(hs.AMQ, Tensors.fromString("{{1/2, -1/8}, {+3/4, -1/8}}"));
  }
}
