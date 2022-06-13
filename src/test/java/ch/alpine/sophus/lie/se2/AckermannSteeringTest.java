// code by jph
package ch.alpine.sophus.lie.se2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.qty.UnitSystem;
import ch.alpine.tensor.sca.Chop;

class AckermannSteeringTest {
  @Test
  void testSimple() {
    AckermannSteering asL = new AckermannSteering(Quantity.of(1, "m"), Quantity.of(+0.4, "m"));
    AckermannSteering asR = new AckermannSteering(Quantity.of(1, "m"), Quantity.of(-0.4, "m"));
    Scalar delta = RealScalar.of(0.2);
    Scalar aL = asL.angle(delta);
    assertTrue(Scalars.lessThan(delta, aL));
    Scalar aR = asR.angle(delta);
    assertTrue(Scalars.lessThan(aR, delta));
  }

  @Test
  void testId() {
    AckermannSteering asL = new AckermannSteering(Quantity.of(1, "m"), Quantity.of(+0, "m"));
    Scalar delta = RealScalar.of(0.2);
    assertEquals(asL.angle(delta), delta);
  }

  @Test
  void testPair() {
    AckermannSteering asL = new AckermannSteering(Quantity.of(1, "m"), Quantity.of(+0.4, "m"));
    Scalar delta = RealScalar.of(0.2);
    Tensor pair = asL.pair(delta);
    assertEquals(pair.Get(0), asL.angle(delta));
    AckermannSteering asR = new AckermannSteering(Quantity.of(1, "m"), Quantity.of(-0.4, "m"));
    assertEquals(pair.Get(1), asR.angle(delta));
  }

  @Test
  void testUnits() {
    ScalarUnaryOperator suo = UnitSystem.SI();
    AckermannSteering asL = new AckermannSteering(suo.apply(Quantity.of(1, "m")), suo.apply(Quantity.of(+40, "cm")));
    Scalar delta = RealScalar.of(0.2);
    Tensor pair = asL.pair(delta);
    Chop._12.requireClose(pair, Tensors.vector(0.21711959572073944, 0.1853540110207382));
  }
}
