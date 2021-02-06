// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class TSnMemberQTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    MemberQ memberQ = Serialization.copy(SnMemberQ.INSTANCE);
    memberQ.require(UnitVector.of(4, 3));
    TSnMemberQ tSnMemberQ = Serialization.copy(new TSnMemberQ(UnitVector.of(4, 3)));
    tSnMemberQ.require(UnitVector.of(4, 2));
  }

  public void testNullFail() {
    AssertFail.of(() -> new TSnMemberQ(null));
  }
}
