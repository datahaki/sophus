// code by jph
package ch.alpine.sophus.lie.he;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.nrm.Vector2Norm;

class HeTargetTest {
  @Test
  public void testFailNegative() {
    assertThrows(Exception.class, () -> new HeTarget(Vector2Norm::of, RealScalar.of(-1)));
  }
}
