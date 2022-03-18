// code by jph
package ch.alpine.sophus.hs.r2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensors;

public class Extract2DTest {
  @Test
  public void testSimple() {
    assertEquals(Extract2D.FUNCTION.apply(Tensors.vector(1, 2, 4)), Tensors.vector(1, 2));
  }

  @Test
  public void testFailScalar() {
    AssertFail.of(() -> Extract2D.FUNCTION.apply(RealScalar.ONE));
  }

  @Test
  public void testFailEmpty() {
    AssertFail.of(() -> Extract2D.FUNCTION.apply(Tensors.empty()));
  }

  @Test
  public void testFailOne() {
    AssertFail.of(() -> Extract2D.FUNCTION.apply(Tensors.vector(1)));
  }
}
