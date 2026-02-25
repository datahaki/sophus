// code by jph
package ch.alpine.sophus.hs.h;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.TensorDistance;
import ch.alpine.tensor.Tensors;

class HLineDistanceTest {
  @Test
  void testSimple() {
    // TensorDistance tensorDistance = HnLineDistance.INSTANCE.tensorNorm(Tensors.vector(1, 0, +1), Tensors.vector(1, 1, -2));
    TensorDistance tensorDistance = //
        HLineDistance.INSTANCE.distanceToLine(Tensors.vector(1, +1), Tensors.vector(1, -2));
    tensorDistance.distance(Tensors.vector(2, 2));
  }
}
