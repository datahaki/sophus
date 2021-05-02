// code by ob
package ch.alpine.sophus.hs.h2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.ExactScalarQ;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class H2GeodesicTest extends TestCase {
  public void testSimple() {
    Tensor split = H2Geodesic.INSTANCE.split(Tensors.vector(1, 1), Tensors.vector(3, 1), RationalScalar.HALF);
    Chop._12.requireClose(split, Tensors.vector(2, Math.sqrt(2)));
  }

  public void testYAxis() {
    Tensor split = H2Geodesic.INSTANCE.split(Tensors.vector(1, 1), Tensors.vector(1, 3), RationalScalar.HALF);
    assertTrue(ExactScalarQ.of(split.Get(0)));
    Chop._12.requireClose(split, Tensors.vector(1, 1.7320508075688772));
  }

  public void testNumeric() {
    Tensor e = Tensors.vector(0.9999999999999999, 2.010051514185878);
    Tensor t = Tensors.vector(1.0, 2.0);
    Tensor split = H2Geodesic.INSTANCE.split(e, t, RationalScalar.HALF);
    Chop._12.requireClose(split, Tensors.vector(1.0, 2.0050194583524013));
  }

  public void testCurve() {
    Tensor e = Tensors.vector(0.9999999999999999, 2.010051514185878);
    Tensor t = Tensors.vector(1.0, 2.0);
    Tensor split = H2Geodesic.INSTANCE.split(e, t, RealScalar.of(0.2));
    assertEquals(split, H2Geodesic.INSTANCE.split(e, t, RealScalar.of(0.2)));
  }

  public void testSingularityExact() {
    AssertFail.of(() -> H2Geodesic.INSTANCE.split(Tensors.vector(1, 0), Tensors.vector(1, 3), RationalScalar.HALF));
  }

  public void testSingularityNumeric() {
    AssertFail.of(() -> H2Geodesic.INSTANCE.split(Tensors.vector(1, 0.0), Tensors.vector(1, 3), RationalScalar.HALF));
  }
}
