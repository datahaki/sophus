// code by jph
package ch.ethz.idsc.sophus.clt;

import java.io.IOException;

import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class Se2CoveringClothoidsTest extends TestCase {
  public void testAngles() throws ClassNotFoundException, IOException {
    Serialization.copy(ClothoidBuilders.SE2_COVERING);
  }
}
