// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.IOException;

import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class GrMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(GrMemberQ.of(Tolerance.CHOP));
  }

  public void testNullFail() {
    try {
      GrMemberQ.of(null);
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
