// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class StochasticMatrixQTest extends TestCase {
  public void testSimple() {
    StochasticMatrixQ.requireRows(IdentityMatrix.of(3), Chop._08);
  }

  public void testScalarFail() {
    AssertFail.of(() -> StochasticMatrixQ.requireRows(RealScalar.ONE, Chop._08));
  }

  public void testVectorFail() {
    AssertFail.of(() -> StochasticMatrixQ.requireRows(Tensors.vector(1, 0, 0), Chop._08));
  }
}
