// code by jph
package ch.alpine.sophus.clt;

import java.io.IOException;

import ch.alpine.tensor.ext.Serialization;
import junit.framework.TestCase;

public class ClothoidEmitTest extends TestCase {
  public void testAngles() throws ClassNotFoundException, IOException {
    Serialization.copy(ClothoidBuilders.SE2_COVERING);
  }
}
