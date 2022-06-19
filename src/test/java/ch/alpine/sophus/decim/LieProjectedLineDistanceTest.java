// code by jph
package ch.alpine.sophus.decim;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.sophus.math.api.TensorNorm;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;

class LieProjectedLineDistanceTest {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    LieProjectedLineDistance lieProjectedLineDistance = //
        new LieProjectedLineDistance(Se2CoveringGroup.INSTANCE);
    TensorNorm tensorNorm = lieProjectedLineDistance.tensorNorm(Tensors.vector(1, 2, 3), Tensors.vector(2, 3, 4));
    Serialization.copy(tensorNorm);
  }
}
