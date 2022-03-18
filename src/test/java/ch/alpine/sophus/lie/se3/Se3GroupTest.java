// code by jph
package ch.alpine.sophus.lie.se3;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;

public class Se3GroupTest {
  @Test
  public void testSimple() {
    Se3Group.INSTANCE.element(IdentityMatrix.of(4));
  }

  @Test
  public void testVectorFail() {
    AssertFail.of(() -> Se3Group.INSTANCE.element(UnitVector.of(4, 1)));
  }

  @Test
  public void testMatrixFail() {
    AssertFail.of(() -> Se3Group.INSTANCE.element(HilbertMatrix.of(3)));
  }
}
