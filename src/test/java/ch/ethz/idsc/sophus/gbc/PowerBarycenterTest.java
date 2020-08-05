// code by jph
package ch.ethz.idsc.sophus.gbc;

import java.io.IOException;

import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class PowerBarycenterTest extends TestCase {
  public void testSerialization() throws ClassNotFoundException, IOException {
    Serialization.copy(PowerBarycenter.of(1.5));
  }

  public void testExpFail() {
    PowerBarycenter.of(0.0);
    PowerBarycenter.of(2.0);
    try {
      PowerBarycenter.of(-0.1);
      fail();
    } catch (Exception exception) {
      // ---
    }
    try {
      PowerBarycenter.of(2.1);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
