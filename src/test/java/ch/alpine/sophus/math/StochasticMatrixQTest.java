// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;

class StochasticMatrixQTest {
  @Test
  void testSimple() {
    StochasticMatrixQ.INSTANCE.requireMember(IdentityMatrix.of(3));
  }

  @Test
  void testScalarFail() {
    assertThrows(Exception.class, () -> StochasticMatrixQ.INSTANCE.requireMember(RealScalar.ONE));
  }

  @Test
  void testVectorFail() {
    assertThrows(Exception.class, () -> StochasticMatrixQ.INSTANCE.requireMember(Tensors.vector(1, 0, 0)));
  }
}
