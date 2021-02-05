// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import junit.framework.TestCase;

public class SnMemberQTest extends TestCase {
  public void testD1() {
    SnMemberQ.INSTANCE.require(Tensors.vector(+1));
    SnMemberQ.INSTANCE.require(Tensors.vector(-1));
  }

  public void testD2() {
    CirclePoints.of(13).stream().forEach(SnMemberQ.INSTANCE::require);
    CirclePoints.of(14).stream().forEach(SnMemberQ.INSTANCE::require);
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    MemberQ memberQ = Serialization.copy(SnMemberQ.INSTANCE);
    memberQ.require(UnitVector.of(4, 3));
    TSnMemberQ tSnMemberQ = Serialization.copy(new TSnMemberQ(UnitVector.of(4, 3)));
    tSnMemberQ.require(UnitVector.of(4, 2));
  }

  public void testNullFail() {
    AssertFail.of(() -> SnMemberQ.INSTANCE.isMember(null));
  }
}
