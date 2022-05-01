// code by jph
package ch.alpine.sophus.decim;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.se2c.Se2CoveringManifold;
import ch.alpine.tensor.ext.Serialization;

class SymmetricLineDistanceTest {
  @Test
  public void testSimple() throws ClassNotFoundException, IOException {
    LineDistance lineDistance = new SymmetricLineDistance(new HsLineDistance(Se2CoveringManifold.INSTANCE));
    Serialization.copy(lineDistance);
  }
}
