// code by jph
package ch.alpine.sophus.lie.gl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.alg.Array;

public class TGlMemberQTest {
  @Test
  public void testSimple() {
    assertTrue(TGlMemberQ.INSTANCE.test(Array.zeros(2, 2)));
    assertFalse(TGlMemberQ.INSTANCE.test(Array.zeros(2, 3)));
  }
}
