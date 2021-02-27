// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.IOException;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.ext.Serialization;
import junit.framework.TestCase;

public class SnAngleTest extends TestCase {
  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(new SnAngle(UnitVector.of(4, 2)));
  }

  public void testMemberQFail() {
    AssertFail.of(() -> new SnAngle(Tensors.vector(1, 1)));
  }
}
