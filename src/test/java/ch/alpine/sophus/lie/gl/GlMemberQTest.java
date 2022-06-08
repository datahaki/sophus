// code by jph
package ch.alpine.sophus.lie.gl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.mat.IdentityMatrix;

class GlMemberQTest {
  @Test
  public void testSimple() {
    assertTrue(GlMemberQ.INSTANCE.test(IdentityMatrix.of(2)));
    assertFalse(GlMemberQ.INSTANCE.test(Array.zeros(2, 2)));
    assertFalse(GlMemberQ.INSTANCE.test(Array.zeros(2, 3)));
  }
}
