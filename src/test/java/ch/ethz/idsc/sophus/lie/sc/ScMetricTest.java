// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.lie.Permutations;
import ch.ethz.idsc.tensor.opt.Pi;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class ScMetricTest extends TestCase {
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

  public void testSimple() {
    _check(Tensors.of(RealScalar.of(2)), Tensors.of(RealScalar.of(3)), Tensors.of(RealScalar.of(5)));
    _check(Tensors.of(RealScalar.of(2)), Tensors.of(Pi.VALUE), Tensors.of(RealScalar.of(0.1)));
  }
}
