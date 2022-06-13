// code by jph
package ch.alpine.sophus.lie.se3;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;

class Se3GroupTest {
  @Test
  void testSimple() {
    Se3Group.INSTANCE.element(IdentityMatrix.of(4));
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> Se3Group.INSTANCE.element(UnitVector.of(4, 1)));
  }

  @Test
  void testMatrixFail() {
    assertThrows(Exception.class, () -> Se3Group.INSTANCE.element(HilbertMatrix.of(3)));
  }
}
