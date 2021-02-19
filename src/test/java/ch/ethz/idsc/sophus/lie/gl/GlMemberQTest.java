// code by jph
package ch.ethz.idsc.sophus.lie.gl;

import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class GlMemberQTest extends TestCase {
  public void testSimple() {
    assertTrue(GlMemberQ.INSTANCE.test(IdentityMatrix.of(2)));
    assertFalse(GlMemberQ.INSTANCE.test(Array.zeros(2, 2)));
    assertFalse(GlMemberQ.INSTANCE.test(Array.zeros(2, 3)));
  }
}
