// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;

public class NilpotentAlgebraQTest {
  @Test
  public void testSimple() {
    assertTrue(NilpotentAlgebraQ.of(new HeAlgebra(1).ad()));
    assertFalse(NilpotentAlgebraQ.of(So3Algebra.INSTANCE.ad()));
    assertFalse(NilpotentAlgebraQ.of(Sl2Algebra.INSTANCE.ad()));
    assertFalse(NilpotentAlgebraQ.of(Se2Algebra.INSTANCE.ad()));
  }
}
