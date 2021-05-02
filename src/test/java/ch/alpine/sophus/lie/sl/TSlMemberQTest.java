// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class TSlMemberQTest extends TestCase {
  public void testSimple() {
    assertFalse(TSlMemberQ.INSTANCE.test(IdentityMatrix.of(2)));
    assertTrue(TSlMemberQ.INSTANCE.test(DiagonalMatrix.of(-1, 3, -2, 0)));
  }
}
