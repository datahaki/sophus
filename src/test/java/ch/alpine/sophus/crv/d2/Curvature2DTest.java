// code by jph
package ch.alpine.sophus.crv.d2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class Curvature2DTest extends TestCase {
  public void testString2() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}}");
    Tensor vector = Curvature2D.string(points);
    Chop._12.requireClose(vector, Tensors.vector(0, 0));
  }

  public void testString3() {
    Tensor points = Tensors.fromString("{{0, 0}, {1, 1}, {2, 0}}");
    Tensor vector = Curvature2D.string(points);
    Chop._12.requireClose(vector, Tensors.vector(-1, -1, -1));
  }

  public void testStringEmpty() {
    Tensor points = Tensors.empty();
    Tensor vector = Curvature2D.string(points);
    assertEquals(points, vector);
  }

  public void testFailHi() {
    Tensor points = Tensors.fromString("{{0, 0, 0}, {1, 1, 0}, {2, 0, 0}}");
    AssertFail.of(() -> Curvature2D.string(points));
  }

  public void testFailStringScalar() {
    AssertFail.of(() -> Curvature2D.string(RealScalar.ZERO));
  }
}
