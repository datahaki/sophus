// code by jph
package ch.ethz.idsc.sophus.clt;

import ch.ethz.idsc.sophus.clt.mid.MidpointTangentOrder2;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;
import junit.framework.TestCase;

public class ClothoidTangentDefectTest extends TestCase {
  public void testSimple() {
    Tolerance.CHOP.requireClose( //
        ClothoidTangentDefect.of(+1.2, 0.5).defect(RealScalar.of(0.2)), //
        RealScalar.of(-2.1006943560956515));
    Tolerance.CHOP.requireClose( //
        ClothoidTangentDefect.of(-0.1, 0.7).defect(RealScalar.of(0.3)), //
        RealScalar.of(-0.22083859316989274));
    Tolerance.CHOP.requireClose( //
        ClothoidTangentDefect.of(+0.1, 0.7).defect(RealScalar.of(0.3)), //
        RealScalar.of(-0.6251498036834668));
  }

  public void testZeroApx() {
    Distribution distribution = UniformDistribution.of(-0.5, 0.5);
    for (int count = 0; count < 100; ++count) {
      Scalar s1 = RandomVariate.of(distribution);
      Scalar s2 = RandomVariate.of(distribution);
      Scalar lam = MidpointTangentOrder2.INSTANCE.apply(s1, s2);
      Chop._02.requireZero(ClothoidTangentDefect.of(s1, s2).defect(lam));
      Sign.requirePositive(ClothoidTangentDefect.of(s1, s2).signum(lam));
    }
  }
}
