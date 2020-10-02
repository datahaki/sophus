// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.io.Serialization;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class HnMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    HsMemberQ memberQ = Serialization.copy(HnMemberQ.of(Tolerance.CHOP));
    memberQ.requirePoint(UnitVector.of(4, 3));
    assertFalse(memberQ.isPoint(UnitVector.of(4, 1)));
  }

  public void testNullFail() {
    AssertFail.of(() -> HnMemberQ.of(null));
  }
}
