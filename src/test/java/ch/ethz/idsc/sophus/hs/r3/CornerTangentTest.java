// code by jph
package ch.ethz.idsc.sophus.hs.r3;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Quaternion;
import ch.ethz.idsc.tensor.Scalar;
import junit.framework.TestCase;

public class CornerTangentTest extends TestCase {
  public void testSimple() {
    Quaternion qi = Quaternion.of(0, 2, 3, 4);
    Quaternion qj = Quaternion.of(0, 1, 8, 2);
    Quaternion qk = Quaternion.of(0, 5, -2, 0);
    Quaternion ql = Quaternion.of(0, -1, -3, 7);
    Quaternion cr = CrossRatio.of(qi, qj, qk, ql);
    Quaternion ct1 = CornerTangent.of(qk, qi, qj);
    Quaternion ct2 = CornerTangent.of(qk, qi, ql);
    Scalar add = ct1.reciprocal().multiply(ct2);
    assertEquals(cr, add);
  }

  public void testImFail1() {
    Quaternion qi = Quaternion.of(1, 2, 3, 4);
    Quaternion qj = Quaternion.of(0, 1, 8, 2);
    Quaternion qk = Quaternion.of(0, 5, -2, 0);
    AssertFail.of(() -> CornerTangent.of(qi, qj, qk));
  }

  public void testImFail2() {
    Quaternion qi = Quaternion.of(0, 2, 3, 4);
    Quaternion qj = Quaternion.of(1, 1, 8, 2);
    Quaternion qk = Quaternion.of(0, 5, -2, 0);
    AssertFail.of(() -> CornerTangent.of(qi, qj, qk));
  }

  public void testImFail3() {
    Quaternion qi = Quaternion.of(0, 2, 3, 4);
    Quaternion qj = Quaternion.of(0, 1, 8, 2);
    Quaternion qk = Quaternion.of(1, 5, -2, 0);
    AssertFail.of(() -> CornerTangent.of(qi, qj, qk));
  }
}
