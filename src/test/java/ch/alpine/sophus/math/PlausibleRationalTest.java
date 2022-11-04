// code by jph
package ch.alpine.sophus.math;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.ComplexScalar;
import ch.alpine.tensor.RationalScalar;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.chq.ExactScalarQ;
import ch.alpine.tensor.sca.Re;

class PlausibleRationalTest {
  @Test
  void testComplex() {
    Scalar scalar = PlausibleRational.FUNCTION.apply(ComplexScalar.of(0.5, 0.2344523));
    assertEquals(RationalScalar.HALF.toString(), Re.FUNCTION.apply(scalar).toString());
  }

  @Test
  void testQuantity() {
    Scalar scalar = PlausibleRational.FUNCTION.apply(Scalars.fromString("0.333333333333333333333333333[m]"));
    assertEquals(scalar.toString(), "1/3[m]");
  }

  @Test
  void testPlausibleRational() {
    Scalar scalar = PlausibleRational.FUNCTION.apply(RealScalar.of(0.0));
    ExactScalarQ.require(scalar);
  }
}
