// code by jph
package ch.alpine.sophus.lie;

import junit.framework.TestCase;

public class NilpotentAlgebraQTest extends TestCase {
  public void testSimple() {
    assertTrue(NilpotentAlgebraQ.of(LieAlgebras.he1()));
    assertFalse(NilpotentAlgebraQ.of(LieAlgebras.so3()));
  }
}
