// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensors;
import junit.framework.TestCase;

public class Extract2DTest extends TestCase {
  public void testSimple() {
    assertEquals(Extract2D.FUNCTION.apply(Tensors.vector(1, 2, 4)), Tensors.vector(1, 2));
  }

  public void testFailScalar() {
    AssertFail.of(() -> Extract2D.FUNCTION.apply(RealScalar.ONE));
  }

  public void testFailEmpty() {
    AssertFail.of(() -> Extract2D.FUNCTION.apply(Tensors.empty()));
  }

  public void testFailOne() {
    AssertFail.of(() -> Extract2D.FUNCTION.apply(Tensors.vector(1)));
  }
}
