// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class SnMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    HsMemberQ memberQ = Serialization.copy(SnMemberQ.of(Tolerance.CHOP));
    memberQ.requirePoint(UnitVector.of(4, 3));
    memberQ.requireTangent(UnitVector.of(4, 3), UnitVector.of(4, 2));
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
