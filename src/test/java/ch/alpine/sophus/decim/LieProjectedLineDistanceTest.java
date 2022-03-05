// code by jph
package ch.alpine.sophus.decim;

import java.io.IOException;

import ch.alpine.sophus.api.TensorNorm;
import ch.alpine.sophus.lie.se2c.Se2CoveringExponential;
import ch.alpine.sophus.lie.se2c.Se2CoveringGroup;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class LieProjectedLineDistanceTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    LieProjectedLineDistance lieProjectedLineDistance = //
        new LieProjectedLineDistance(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE);
    TensorNorm tensorNorm = lieProjectedLineDistance.tensorNorm(Tensors.vector(1, 2, 3), Tensors.vector(2, 3, 4));
    Serialization.copy(tensorNorm);
  }
}
