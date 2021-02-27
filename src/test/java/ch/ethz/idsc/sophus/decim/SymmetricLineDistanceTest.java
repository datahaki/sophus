// code by jph
package ch.ethz.idsc.sophus.decim;

import java.io.IOException;

import ch.ethz.idsc.sophus.lie.se2c.Se2CoveringManifold;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class SymmetricLineDistanceTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    LineDistance lineDistance = new SymmetricLineDistance(new HsLineDistance(Se2CoveringManifold.INSTANCE));
    Serialization.copy(lineDistance);
  }
}
