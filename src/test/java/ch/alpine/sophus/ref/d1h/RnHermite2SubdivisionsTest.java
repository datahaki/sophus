// code by jph
package ch.alpine.sophus.ref.d1h;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

class RnHermite2SubdivisionsTest {
  @Test
  void testA1() {
    RnHermite2Subdivision a1 = RnHermite2Subdivisions.standard();
    // lambda == -1/8, mu == -1/2
    RnHermite2Subdivision hs = RnHermite2Subdivisions.of(RationalScalar.of(-1, 8), RationalScalar.of(-1, 2));
    assertEquals(a1.ALP(), hs.ALP());
    assertEquals(a1.ALQ(), hs.ALQ());
    assertEquals(a1.AHP(), hs.AHP());
    assertEquals(a1.AHQ(), hs.AHQ());
  }

  @Test
  void testMoreA1() {
    Tensor ALP = Tensors.fromString("{{27/32, +9/64}, {-9/16,  3/32}}"); // A(+0)
    Tensor ALQ = Tensors.fromString("{{ 5/32, -3/64}, {+9/16, -5/32}}"); // A(-2)
    Tensor AHP = Tensors.fromString("{{ 5/32, +3/64}, {-9/16, -5/32}}"); // A(+1)
    Tensor AHQ = Tensors.fromString("{{27/32, -9/64}, {+9/16,  3/32}}"); // A(-1)
    RnHermite2Subdivision hs = RnHermite2Subdivisions.standard();
    assertEquals(hs.ALP(), ALP);
    assertEquals(hs.ALQ(), ALQ);
    assertEquals(hs.AHP(), AHP);
    assertEquals(hs.AHQ(), AHQ);
  }

  @Test
  void testA2() {
    RnHermite2Subdivision a2 = RnHermite2Subdivisions.manifold();
    // lambda == -1/5, mu == 9/10
    RnHermite2Subdivision hs = RnHermite2Subdivisions.of(RationalScalar.of(-1, 5), RationalScalar.of(9, 10));
    assertEquals(a2.ALP(), hs.ALP());
    assertEquals(a2.ALQ(), hs.ALQ());
    assertEquals(a2.AHP(), hs.AHP());
    assertEquals(a2.AHQ(), hs.AHQ());
  }

  @Test
  void testMoreA2() {
    Tensor ALP = Tensors.fromString("{{19/25, +31/200}, {-29/400, 277/800}}"); // A(+0)
    Tensor ALQ = Tensors.fromString("{{ 6/25, -29/200}, {+29/400,  65/800}}"); // A(-2)
    Tensor AHP = Tensors.fromString("{{ 6/25, +29/200}, {-29/400,  65/800}}"); // A(+1)
    Tensor AHQ = Tensors.fromString("{{19/25, -31/200}, {+29/400, 277/800}}"); // A(-1)
    RnHermite2Subdivision hs = RnHermite2Subdivisions.manifold();
    assertEquals(hs.ALP(), ALP);
    assertEquals(hs.ALQ(), ALQ);
    assertEquals(hs.AHP(), AHP);
    assertEquals(hs.AHQ(), AHQ);
  }
}
