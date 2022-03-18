// code by jph
package ch.alpine.sophus.math;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;

public class StochasticMatrixQTest {
  @Test
  public void testSimple() {
    StochasticMatrixQ.requireRows(IdentityMatrix.of(3), Chop._08);
  }

  @Test
  public void testScalarFail() {
    AssertFail.of(() -> StochasticMatrixQ.requireRows(RealScalar.ONE, Chop._08));
  }

  @Test
  public void testVectorFail() {
    AssertFail.of(() -> StochasticMatrixQ.requireRows(Tensors.vector(1, 0, 0), Chop._08));
  }
}
