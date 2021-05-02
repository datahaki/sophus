// code by jph
package ch.alpine.sophus.clt.mid;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.NormalDistribution;
import ch.alpine.tensor.pdf.RandomVariate;
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
      Scalar f2 = MidpointTangentApproximation.ORDER2.apply(b0, b1);
      Tolerance.CHOP.requireClose(f1, f2);
    }
  }
}