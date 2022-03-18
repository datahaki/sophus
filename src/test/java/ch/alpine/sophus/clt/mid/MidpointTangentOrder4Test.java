// code by jph
package ch.alpine.sophus.clt.mid;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.NormalDistribution;

public class MidpointTangentOrder4Test {
  @Test
  public void testSymmetry() {
    for (int count = 0; count < 100; ++count) {
      Scalar s1 = RandomVariate.of(NormalDistribution.standard());
      Scalar s2 = RandomVariate.of(NormalDistribution.standard());
      Scalar f1 = MidpointTangentOrder4.INSTANCE.apply(s1, s2);
      Scalar f2 = MidpointTangentOrder4.INSTANCE.apply(s1.negate(), s2).negate(); // odd
      Scalar f3 = MidpointTangentOrder4.INSTANCE.apply(s1, s2.negate());
      Scalar f4 = MidpointTangentOrder4.INSTANCE.apply(s1.negate(), s2.negate()).negate();
      Tolerance.CHOP.requireClose(f1, f2);
      Tolerance.CHOP.requireClose(f2, f3);
      Tolerance.CHOP.requireClose(f3, f4);
      Tolerance.CHOP.requireClose(f4, f1);
    }
  }

  @Test
  public void test4MathematicaSync() {
    Scalar scalar = MidpointTangentOrder4.INSTANCE.apply(RealScalar.of(1), RealScalar.of(2));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(-1.055975742266341));
  }

  @Test
  public void test5MathematicaSync() {
    Scalar scalar = MidpointTangentOrder5.INSTANCE.apply(RealScalar.of(1), RealScalar.of(2));
    Tolerance.CHOP.requireClose(scalar, RealScalar.of(-1.0621982931895297));
  }
}
