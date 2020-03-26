// code by jph
package ch.ethz.idsc.sophus.crv.clothoid;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import junit.framework.TestCase;

public class MidpointTangentOrder2Test extends TestCase {
  public void testSimple() {
    for (int count = 0; count < 100; ++count) {
      Scalar s1 = RandomVariate.of(NormalDistribution.standard());
      Scalar s2 = RandomVariate.of(NormalDistribution.standard());
      Scalar f1 = MidpointTangentOrder2.INSTANCE.apply(s1, s2);
      Scalar f2 = MidpointTangentOrder2.INSTANCE.apply(s1.negate(), s2).negate(); // odd
      Scalar f3 = MidpointTangentOrder2.INSTANCE.apply(s1, s2.negate());
      Scalar f4 = MidpointTangentOrder2.INSTANCE.apply(s1.negate(), s2.negate()).negate();
      Tolerance.CHOP.requireClose(f1, f2);
      Tolerance.CHOP.requireClose(f2, f3);
      Tolerance.CHOP.requireClose(f3, f4);
      Tolerance.CHOP.requireClose(f4, f1);
    }
  }
}
