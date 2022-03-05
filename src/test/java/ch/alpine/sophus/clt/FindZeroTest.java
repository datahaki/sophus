// code by jph
package ch.alpine.sophus.clt;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.num.Pi;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.tri.Cos;
import junit.framework.TestCase;

public class FindZeroTest extends TestCase {
  public void testSimple() {
    FindZero findZero = new FindZero(Cos.FUNCTION, Sign::isPositive, Chop._12);
    Chop._10.requireClose(findZero.between(RealScalar.of(0.0), RealScalar.of(4.0)), Pi.HALF);
    Chop._10.requireClose(findZero.between(RealScalar.of(1.0), RealScalar.of(4.0)), Pi.HALF);
    Chop._10.requireClose(findZero.between(RealScalar.of(1.0), RealScalar.of(2.0)), Pi.HALF);
  }

  public void testLinear() {
    Scalar scalar = FindZero.linear(RealScalar.of(10), RealScalar.of(11), RealScalar.of(5), RealScalar.of(-2));
    assertEquals(scalar, RationalScalar.of(75, 7));
  }

  public void testOther() {
    Scalar scalar = FindZero.linear(RealScalar.of(5), RealScalar.of(6), RealScalar.of(2), RealScalar.of(-1));
    assertEquals(scalar, RationalScalar.of(5 * 3 + 2, 3));
  }
}
