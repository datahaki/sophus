// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;

class StochasticMatrixQTest {
  @Test
  void testSimple() {
    StochasticMatrixQ.requireRows(IdentityMatrix.of(3), Chop._08);
  }

  @Test
  void testScalarFail() {
    assertThrows(Exception.class, () -> StochasticMatrixQ.requireRows(RealScalar.ONE, Chop._08));
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> StochasticMatrixQ.requireRows(Tensors.vector(1, 0, 0), Chop._08));
  }
}
