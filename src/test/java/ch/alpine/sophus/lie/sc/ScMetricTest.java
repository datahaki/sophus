// code by jph
package ch.alpine.sophus.lie.sc;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

class ScMetricTest {
  private static void _check(Tensor p, Tensor q, Tensor r) {
    Scalar pq = ScMetric.INSTANCE.distance(p, q);
    Sign.requirePositive(pq);
    Scalar qp = ScMetric.INSTANCE.distance(q, p);
    Chop._10.requireClose(pq, qp);
    for (Tensor perm : Permutations.of(Tensors.of(p, q, r))) {
      Tensor p0 = perm.get(0);
      Tensor p1 = perm.get(1);
      Tensor p2 = perm.get(2);
      Scalar d01 = ScMetric.INSTANCE.distance(p0, p1);
      Scalar d12 = ScMetric.INSTANCE.distance(p1, p2);
      Scalar d02 = ScMetric.INSTANCE.distance(p0, p2);
      Sign.requirePositiveOrZero(Chop._10.apply(d01.add(d12).subtract(d02)));
    }
  }

  @Test
  void testSimple() {
    _check(Tensors.of(RealScalar.of(2)), Tensors.of(RealScalar.of(3)), Tensors.of(RealScalar.of(5)));
    _check(Tensors.of(RealScalar.of(2)), Tensors.of(Pi.VALUE), Tensors.of(RealScalar.of(0.1)));
  }
}
