// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.tensor.alg.Array;
import junit.framework.TestCase;

public class TGlMemberQTest extends TestCase {
  public void testSimple() {
    assertTrue(TGlMemberQ.INSTANCE.test(Array.zeros(2, 2)));
    assertFalse(TGlMemberQ.INSTANCE.test(Array.zeros(2, 3)));
  }
}
