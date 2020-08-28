// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class HnMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(HnMemberQ.of(Tolerance.CHOP));
  }

  public void testNullFail() {
    try {
      HnMemberQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
