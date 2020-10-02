// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import junit.framework.TestCase;

public class StochasticMatrixQTest extends TestCase {
  public void testSimple() {
    StochasticMatrixQ.requireRows(IdentityMatrix.of(3));
  }

  public void testScalarFail() {
    AssertFail.of(() -> StochasticMatrixQ.requireRows(RealScalar.ONE));
  }

  public void testVectorFail() {
    AssertFail.of(() -> StochasticMatrixQ.requireRows(Tensors.vector(1, 0, 0)));
  }
}
