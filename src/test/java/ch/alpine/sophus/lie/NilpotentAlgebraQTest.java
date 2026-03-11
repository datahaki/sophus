// code by jph
package ch.alpine.sophus.lie;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.he.HeAlgebra;
import ch.alpine.sophus.lie.sl.Sl2Algebra;
import ch.alpine.sophus.lie.so.So3Group;
import ch.alpine.tensor.Tensor;

class NilpotentAlgebraQTest {
  @Test
  void testSimple() {
    assertTrue(NilpotentAlgebraQ.of(new HeAlgebra(1).ad()));
    Tensor ad = MatrixAlgebra.of(So3Group.INSTANCE).ad();
    assertFalse(NilpotentAlgebraQ.of(ad));
    assertFalse(NilpotentAlgebraQ.of(Sl2Algebra.INSTANCE.ad()));
    assertFalse(NilpotentAlgebraQ.of(LieAlgebraAds.se(2)));
  }
}
