// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

public class TurningGeometryTest {
  @Test
  public void test90() {
    Optional<Scalar> offsetY = TurningGeometry.offset_y(RealScalar.ONE, RealScalar.of(Math.PI / 2));
    Chop._10.requireZero(offsetY.get());
  }

  @Test
  public void test45() {
    Optional<Scalar> offsetY = TurningGeometry.offset_y(RealScalar.ONE, RealScalar.of(Math.PI / 4));
    Chop._10.requireClose(offsetY.get(), RealScalar.ONE);
  }

  @Test
  public void test45neg() {
    Optional<Scalar> offsetY = TurningGeometry.offset_y(RealScalar.ONE, RealScalar.of(-Math.PI / 4));
    Chop._10.requireClose(offsetY.get(), RealScalar.ONE.negate());
  }

  @Test
  public void test0() {
    Optional<Scalar> offsetY = TurningGeometry.offset_y(RealScalar.ONE, RealScalar.ZERO);
    assertFalse(offsetY.isPresent());
  }

  @Test
  public void testClose0() {
    Optional<Scalar> offsetY = TurningGeometry.offset_y(RealScalar.ONE, RealScalar.of(TurningGeometry.CHOP.threshold()));
    assertTrue(offsetY.isPresent());
  }

  @Test
  public void test45Units() {
    Optional<Scalar> offsetY = TurningGeometry.offset_y(Quantity.of(1.23, "m"), RealScalar.of(0.345));
    Chop._10.requireClose(offsetY.get(), Quantity.of(3.4226321090018064, "m"));
  }

  @Test
  public void testRatio() {
    Scalar angle = Quantity.of(0.23, "m^-1");
    Optional<Scalar> offsetY = TurningGeometry.offset_y(angle);
    assertEquals(offsetY.get(), angle.reciprocal());
  }
}
