// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import junit.framework.TestCase;

public class FresnelCurveTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-100, 100);
    for (int count = 0; count < 100; ++count) {
      Scalar scalar = RandomVariate.of(distribution);
      Tensor p = FresnelCurve.FUNCTION.apply(scalar);
      Tensor q = FresnelCurve.FUNCTION.apply(scalar.negate());
      VectorQ.requireLength(p, 3);
      Tolerance.CHOP.requireClose(p.extract(0, 2), q.extract(0, 2).negate());
      Tolerance.CHOP.requireClose(p.get(2), q.get(2));
    }
  }
}
