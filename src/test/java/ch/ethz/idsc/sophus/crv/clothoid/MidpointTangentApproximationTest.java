// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class MidpointTangentApproximationTest extends TestCase {
  public void testSimple() {
    Scalar f = OriginalApproximation.INSTANCE.apply(RealScalar.of(0.3), RealScalar.of(-0.82));
    Tolerance.CHOP.requireClose(f, RealScalar.of(0.1213890127877238));
  }

  public void testRandom() {
    for (int count = 0; count < 100; ++count) {
      Scalar b0 = RandomVariate.of(NormalDistribution.standard());
      Scalar b1 = RandomVariate.of(NormalDistribution.standard());
      Scalar f1 = OriginalApproximation.INSTANCE.apply(b0, b1);
      Scalar f2 = MidpointTangentApproximation.INSTANCE.apply(b0, b1);
      Tolerance.CHOP.requireClose(f1, f2);
    }
  }
}
