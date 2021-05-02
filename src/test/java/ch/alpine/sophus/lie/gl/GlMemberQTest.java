// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class GlMemberQTest extends TestCase {
  public void testSimple() {
    assertTrue(GlMemberQ.INSTANCE.test(IdentityMatrix.of(2)));
    assertFalse(GlMemberQ.INSTANCE.test(Array.zeros(2, 2)));
    assertFalse(GlMemberQ.INSTANCE.test(Array.zeros(2, 3)));
  }
}
