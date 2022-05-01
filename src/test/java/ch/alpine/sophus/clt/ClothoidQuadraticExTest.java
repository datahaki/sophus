// code by jph
package ch.alpine.sophus.clt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.chq.ExactScalarQ;

public class ClothoidQuadraticExTest {
  @Test
  public void testSimple() {
    ClothoidQuadraticEx clothoidQuadratic = //
        new ClothoidQuadraticEx(RealScalar.of(2), RealScalar.of(-3), RealScalar.of(7));
    Scalar p0 = clothoidQuadratic.angle(RealScalar.ZERO);
    Scalar pm = clothoidQuadratic.angle(RationalScalar.of(1, 2));
    Scalar p1 = clothoidQuadratic.angle(RealScalar.ONE);
    assertEquals(p0, RealScalar.of(2));
    assertEquals(pm, RealScalar.of(-3));
    assertEquals(p1, RealScalar.of(7));
  }

  @Test
  public void testExamples() {
    ClothoidQuadraticEx clothoidQuadratic = //
        new ClothoidQuadraticEx(RealScalar.of(5), RealScalar.of(7), RealScalar.of(13));
    Scalar angle = clothoidQuadratic.angle(RealScalar.of(11));
    assertEquals(ExactScalarQ.require(angle), RealScalar.of(973));
  }
}
