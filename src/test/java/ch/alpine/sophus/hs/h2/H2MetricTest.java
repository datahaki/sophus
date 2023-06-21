// code by ob
package ch.alpine.sophus.hs.h2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.exp.Log;
import ch.alpine.tensor.sca.tri.ArcSinh;

class H2MetricTest {
  @Test
  void testTrivial() {
    Tensor p = Tensors.vector(-Math.random(), Math.random());
    Scalar actual = H2Metric.INSTANCE.distance(p, p);
    assertEquals(RealScalar.ZERO, actual);
  }

  @Test
  void testYAxis() {
    Tensor p = Tensors.vector(2, 1 + Math.random());
    Tensor q = Tensors.vector(2, 7 + Math.random());
    // ---
    Scalar actual = H2Metric.INSTANCE.distance(p, q);
    Scalar expected = Abs.FUNCTION.apply(Log.FUNCTION.apply(q.Get(1).divide(p.Get(1))));
    Chop._12.requireClose(actual, expected);
  }

  @Test
  void testXAxis() {
    Tensor p = Tensors.vector(1 + Math.random(), 3);
    Tensor q = Tensors.vector(7 + Math.random(), 3);
    // ---
    Scalar actual = H2Metric.INSTANCE.distance(p, q);
    Scalar expected = RealScalar.of(2).multiply(ArcSinh.FUNCTION.apply( //
        Abs.between(q.Get(0), p.Get(0)).divide(p.Get(1).add(p.Get(1)))));
    Chop._12.requireClose(actual, expected);
  }

  @Test
  void testNegativeY() {
    Tensor p = Tensors.vector(1, 3);
    Tensor q = Tensors.vector(2, -Math.random());
    assertThrows(Exception.class, () -> H2Metric.INSTANCE.distance(p, q));
  }
}
