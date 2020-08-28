// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class SnMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(SnMemberQ.of(Tolerance.CHOP));
  }

  public void testNullFail() {
    try {
      SnMemberQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
