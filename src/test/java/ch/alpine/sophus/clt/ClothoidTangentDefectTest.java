// code by jph
package ch.alpine.sophus.clt;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.clt.mid.MidpointTangentOrder2;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

public class ClothoidTangentDefectTest {
  @Test
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

  @Test
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
