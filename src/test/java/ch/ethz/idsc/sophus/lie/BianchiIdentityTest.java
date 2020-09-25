// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class BianchiIdentityTest extends TestCase {
  public void testRequireTrivial() {
    BianchiIdentity.require(Array.zeros(4, 4, 4, 4));
  }

  public void testRank1Fail() {
    try {
      BianchiIdentity.of(Tensors.vector(1, 2, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRank2Fail() {
    try {
      BianchiIdentity.of(HilbertMatrix.of(3, 3));
      fail();
    } catch (Exception exception) {
      // ---
    }
  }

  public void testRank3Fail() {
    try {
      BianchiIdentity.of(LieAlgebras.he1());
      fail();
    } catch (Exception exception) {
      // ---
    }
  }
}
