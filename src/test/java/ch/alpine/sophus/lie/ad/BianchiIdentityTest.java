// code by jph
package ch.alpine.sophus.lie.ad;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.HilbertMatrix;
import junit.framework.TestCase;

public class BianchiIdentityTest extends TestCase {
  public void testRequireTrivial() {
    BianchiIdentity.require(Array.zeros(4, 4, 4, 4));
  }

  public void testRank1Fail() {
    AssertFail.of(() -> BianchiIdentity.of(Tensors.vector(1, 2, 3)));
  }

  public void testRank2Fail() {
    AssertFail.of(() -> BianchiIdentity.of(HilbertMatrix.of(3, 3)));
  }

  public void testRank3Fail() {
    AssertFail.of(() -> BianchiIdentity.of(TestHelper.he1()));
  }
}
