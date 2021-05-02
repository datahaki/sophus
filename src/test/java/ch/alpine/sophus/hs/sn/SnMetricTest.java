// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.UnitVector;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.sca.ArcCos;
import ch.alpine.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnMetricTest extends TestCase {
  private static Scalar _check(Tensor p, Tensor q) {
    return ArcCos.FUNCTION.apply((Scalar) p.dot(q)); // complex number if |p.q| > 1
  }

  public void testSimple() {
    Chop._12.requireClose(SnMetric.INSTANCE.distance(UnitVector.of(3, 0), UnitVector.of(3, 1)), Pi.HALF);
    Chop._12.requireClose(SnMetric.INSTANCE.distance(UnitVector.of(3, 0), UnitVector.of(3, 2)), Pi.HALF);
    Chop._12.requireClose(SnMetric.INSTANCE.distance(UnitVector.of(3, 1), UnitVector.of(3, 2)), Pi.HALF);
  }

  public void testDirect() {
    Distribution distribution = NormalDistribution.standard();
    for (int count = 0; count < 100; ++count) {
      Tensor p = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Tensor q = Vector2Norm.NORMALIZE.apply(RandomVariate.of(distribution, 3));
      Scalar a1 = SnMetric.INSTANCE.distance(p, q);
      Scalar a2 = _check(p, q);
      Chop._12.requireClose(a1, a2);
      Scalar norm = Vector2Norm.of(new SnExponential(p).log(q));
      Tolerance.CHOP.requireClose(norm, a1);
    }
  }

  public void testMemberQFail() {
    AssertFail.of(() -> SnMetric.INSTANCE.distance(Tensors.vector(1, 0), Tensors.vector(1, 1)));
    AssertFail.of(() -> SnMetric.INSTANCE.distance(Tensors.vector(1, 1), Tensors.vector(1, 0)));
  }
}