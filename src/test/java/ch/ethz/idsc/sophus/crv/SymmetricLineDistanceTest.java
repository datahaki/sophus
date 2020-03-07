// code by jph
package ch.ethz.idsc.sophus.crv;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringExponential;
import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringGroup;
import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class SymmetricLineDistanceTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    LineDistance lineDistance = new SymmetricLineDistance( //
        new LieGroupLineDistance(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log));
    Serialization.copy(lineDistance);
  }
}
