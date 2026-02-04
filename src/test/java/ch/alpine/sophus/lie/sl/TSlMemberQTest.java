// code by jph
package ch.alpine.sophus.lie.sl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.mat.DiagonalMatrix;
import ch.alpine.tensor.mat.IdentityMatrix;

class TSlMemberQTest {
  @Test
  void testSimple() {
    assertFalse(TSlMemberQ.INSTANCE.isMember(IdentityMatrix.of(2)));
    assertTrue(TSlMemberQ.INSTANCE.isMember(DiagonalMatrix.of(-1, 3, -2, 0)));
  }
}
