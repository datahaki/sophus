// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.sca.Chop;
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
