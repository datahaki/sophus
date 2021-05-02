// code by jph
package ch.alpine.sophus.lie.se3;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.HilbertMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class Se3GroupTest extends TestCase {
  public void testSimple() {
    Se3Group.INSTANCE.element(IdentityMatrix.of(4));
  }

  public void testVectorFail() {
    AssertFail.of(() -> Se3Group.INSTANCE.element(UnitVector.of(4, 1)));
  }

  public void testMatrixFail() {
    AssertFail.of(() -> Se3Group.INSTANCE.element(HilbertMatrix.of(3)));
  }
}
