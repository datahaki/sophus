// code by jph
package ch.alpine.sophus.hs.sn;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.ext.Serialization;

public class SnAngleTest {
  @Test
  public void testSerializable() throws ClassNotFoundException, IOException {
    Serialization.copy(new SnAngle(UnitVector.of(4, 2)));
  }

  @Test
  public void testMemberQFail() {
    AssertFail.of(() -> new SnAngle(Tensors.vector(1, 1)));
  }
}
