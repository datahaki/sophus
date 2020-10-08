// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.mat.Tolerance;
import junit.framework.TestCase;

public class SnMemberQTest extends TestCase {
  public void testD1() {
    HsMemberQ hsMemberQ = SnMemberQ.of(Tolerance.CHOP);
    hsMemberQ.requirePoint(Tensors.vector(+1));
    hsMemberQ.requirePoint(Tensors.vector(-1));
  }

  public void testD2() {
    HsMemberQ hsMemberQ = SnMemberQ.of(Tolerance.CHOP);
    CirclePoints.of(13).stream().forEach(hsMemberQ::requirePoint);
    CirclePoints.of(14).stream().forEach(hsMemberQ::requirePoint);
  }

  public void testSerializable() throws ClassNotFoundException, IOException {
    HsMemberQ memberQ = Serialization.copy(SnMemberQ.of(Tolerance.CHOP));
    memberQ.requirePoint(UnitVector.of(4, 3));
    memberQ.requireTangent(UnitVector.of(4, 3), UnitVector.of(4, 2));
  }

  public void testNullFail() {
    AssertFail.of(() -> SnMemberQ.of(null));
  }
}
