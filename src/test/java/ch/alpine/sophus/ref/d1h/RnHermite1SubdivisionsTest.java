// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensors;

class RnHermite1SubdivisionsTest {
  @Test
  public void testSimple() {
    RnHermite1Subdivision hs = RnHermite1Subdivisions.of(RationalScalar.of(-1, 8), RationalScalar.of(-1, 2));
    assertEquals(hs.AMP, Tensors.fromString("{{1/2, +1/8}, {-3/4, -1/8}}"));
    assertEquals(hs.AMQ, Tensors.fromString("{{1/2, -1/8}, {+3/4, -1/8}}"));
  }
}
