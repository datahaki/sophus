// code by jph
package ch.alpine.sophus.lie;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.se2.Se2Algebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so3.So3Algebra;
import junit.framework.TestCase;

public class NilpotentAlgebraQTest extends TestCase {
  public void testSimple() {
    assertTrue(NilpotentAlgebraQ.of(new HeAlgebra(1).ad()));
    assertFalse(NilpotentAlgebraQ.of(So3Algebra.INSTANCE.ad()));
    assertFalse(NilpotentAlgebraQ.of(Sl2Algebra.INSTANCE.ad()));
    assertFalse(NilpotentAlgebraQ.of(Se2Algebra.INSTANCE.ad()));
  }
}
