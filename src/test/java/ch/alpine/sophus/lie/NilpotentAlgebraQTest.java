// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so.So3Group;

class NilpotentAlgebraQTest {
  @Test
  void testSimple() {
    assertTrue(NilpotentAlgebraQ.of(new HeAlgebra(1).ad()));
    MatrixAlgebra matrixAlgebra = new MatrixAlgebra(So3Group.INSTANCE.matrixBasis());
    assertFalse(NilpotentAlgebraQ.of(matrixAlgebra.ad()));
    assertFalse(NilpotentAlgebraQ.of(Sl2Algebra.INSTANCE.ad()));
    assertFalse(NilpotentAlgebraQ.of(LieAlgebraAds.se(2)));
  }
}
