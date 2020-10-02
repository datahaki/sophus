// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.alg.UnitVector;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
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
