// code by jph
package ch.ethz.idsc.sophus.crv;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class LieProjectedLineDistanceTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    LieProjectedLineDistance lieProjectedLineDistance = //
        new LieProjectedLineDistance(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE);
    TensorNorm tensorNorm = lieProjectedLineDistance.tensorNorm(Tensors.vector(1, 2, 3), Tensors.vector(2, 3, 4));
    Serialization.copy(tensorNorm);
  }
}
