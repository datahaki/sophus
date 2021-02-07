// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class HnAngleTest extends TestCase {
  public void testSimple() throws ClassNotFoundException, IOException {
    HnAngle hnAngle = Serialization.copy(new HnAngle(UnitVector.of(4, 3)));
    assertTrue(Scalars.isZero(hnAngle.apply(UnitVector.of(4, 3))));
    AssertFail.of(() -> hnAngle.log(UnitVector.of(4, 2)));
    AssertFail.of(() -> hnAngle.vectorLog(UnitVector.of(4, 2)));
    AssertFail.of(() -> hnAngle.apply(UnitVector.of(4, 2)));
  }

  public void testMemberFail() {
    AssertFail.of(() -> new HnAngle(UnitVector.of(4, 2)));
  }
}
