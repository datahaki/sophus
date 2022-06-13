// code by jph
package ch.alpine.sophus.hs.sn;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.hs.r3s2.R3S2Geodesic;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;

class SnActionTest {
  @Test
  void testSimple() {
    Scalar scalar = RealScalar.of(0.5);
    Tensor tensor = R3S2Geodesic.INSTANCE.split( //
        Tensors.fromString("{{0, 0, 0}, {1, 0, 0}}"), //
        Tensors.fromString("{{1, 0, 0}, {0, 1, 0}}"), scalar);
    Chop._11.requireClose(tensor, //
        Tensors.fromString("{{0.5, -0.20710678118654752, 0.0}, {0.7071067811865476, 0.7071067811865475, 0.0}}"));
  }
}
