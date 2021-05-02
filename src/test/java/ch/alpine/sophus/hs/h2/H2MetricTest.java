// code by ob
package ch.alpine.sophus.hs.h2;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.ArcSinh;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Log;
import junit.framework.TestCase;

public class H2MetricTest extends TestCase {
  public void testTrivial() {
    Tensor p = Tensors.vector(-Math.random(), Math.random());
    Scalar actual = H2Metric.INSTANCE.distance(p, p);
    assertEquals(RealScalar.ZERO, actual);
  }

  public void testYAxis() {
    Tensor p = Tensors.vector(2, 1 + Math.random());
    Tensor q = Tensors.vector(2, 7 + Math.random());
    // ---
    Scalar actual = H2Metric.INSTANCE.distance(p, q);
    Scalar expected = Abs.FUNCTION.apply(Log.of(q.Get(1).divide(p.Get(1))));
    Chop._12.requireClose(actual, expected);
  }

  public void testXAxis() {
    Tensor p = Tensors.vector(1 + Math.random(), 3);
    Tensor q = Tensors.vector(7 + Math.random(), 3);
    // ---
    Scalar actual = H2Metric.INSTANCE.distance(p, q);
    Scalar expected = RealScalar.of(2).multiply(ArcSinh.of( //
        Abs.between(q.Get(0), p.Get(0)).divide(p.Get(1).add(p.Get(1)))));
    Chop._12.requireClose(actual, expected);
  }

  public void testNegativeY() {
    Tensor p = Tensors.vector(1, 3);
    Tensor q = Tensors.vector(2, -Math.random());
    AssertFail.of(() -> H2Metric.INSTANCE.distance(p, q));
  }
}