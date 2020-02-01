// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.io.IOException;

import ch.ethz.idsc.tensor.io.Serialization;
import junit.framework.TestCase;

public class PowerBarycenterTest extends TestCase {
  public void testSerialization() throws ClassNotFoundException, IOException {
    Serialization.copy(PowerBarycenter.of(1.5));
  }
}
