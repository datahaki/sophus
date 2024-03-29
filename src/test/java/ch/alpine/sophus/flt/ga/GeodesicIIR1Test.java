// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGroup;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.ext.Serialization;
import ch.alpine.tensor.qty.Quantity;
import ch.alpine.tensor.sca.Chop;

class GeodesicIIR1Test {
  @Test
  void testSimple() throws ClassNotFoundException, IOException {
    GeodesicIIR1 geodesicIIR1 = //
        Serialization.copy(new GeodesicIIR1(RnGroup.INSTANCE, RealScalar.of(0.5)));
    // irc=0.0[s^-2]
    // irc=1.9999999999999996[s^-2]
    // irc=1.0000000000000009[s^-2]
    // irc=1.9999999999999996[s^-2]
    // irc=-1.5000000000000002[s^-2]
    // irc=1.0000000000000009[s^-2]
    Scalar acc0 = (Scalar) geodesicIIR1.apply(Quantity.of(0, "s^-2"));
    Chop._13.requireClose(acc0, Quantity.of(0, "s^-2"));
    Scalar acc1 = (Scalar) geodesicIIR1.apply(Quantity.of(2, "s^-2"));
    Chop._13.requireClose(acc1, Quantity.of(1, "s^-2"));
    Scalar acc2 = (Scalar) geodesicIIR1.apply(Quantity.of(1, "s^-2"));
    Chop._13.requireClose(acc2, Quantity.of(1, "s^-2"));
    Scalar acc3 = (Scalar) geodesicIIR1.apply(Quantity.of(2, "s^-2"));
    Chop._13.requireClose(acc3, Quantity.of(1.5, "s^-2"));
    Scalar acc4 = (Scalar) geodesicIIR1.apply(Quantity.of(-1.5, "s^-2"));
    Chop._13.requireClose(acc4, Quantity.of(0, "s^-2"));
    Scalar acc5 = (Scalar) geodesicIIR1.apply(Quantity.of(1, "s^-2"));
    Chop._13.requireClose(acc5, Quantity.of(0.5, "s^-2"));
  }

  @Test
  void testInitialized() {
    GeodesicIIR1 geodesicIIR1 = new GeodesicIIR1(RnGroup.INSTANCE, RealScalar.of(0.5));
    geodesicIIR1.apply(Quantity.of(0, "s^-2"));
    // irc=0.0[s^-2]
    // irc=1.9999999999999996[s^-2]
    // irc=1.0000000000000009[s^-2]
    // irc=1.9999999999999996[s^-2]
    // irc=-1.5000000000000002[s^-2]
    // irc=1.0000000000000009[s^-2]
    Scalar acc0 = (Scalar) geodesicIIR1.apply(Quantity.of(0, "s^-2"));
    Chop._13.requireClose(acc0, Quantity.of(0, "s^-2"));
    Scalar acc1 = (Scalar) geodesicIIR1.apply(Quantity.of(2, "s^-2"));
    Chop._13.requireClose(acc1, Quantity.of(1, "s^-2"));
    Scalar acc2 = (Scalar) geodesicIIR1.apply(Quantity.of(1, "s^-2"));
    Chop._13.requireClose(acc2, Quantity.of(1, "s^-2"));
    Scalar acc3 = (Scalar) geodesicIIR1.apply(Quantity.of(2, "s^-2"));
    Chop._13.requireClose(acc3, Quantity.of(1.5, "s^-2"));
    Scalar acc4 = (Scalar) geodesicIIR1.apply(Quantity.of(-1.5, "s^-2"));
    Chop._13.requireClose(acc4, Quantity.of(0, "s^-2"));
    Scalar acc5 = (Scalar) geodesicIIR1.apply(Quantity.of(1, "s^-2"));
    Chop._13.requireClose(acc5, Quantity.of(0.5, "s^-2"));
  }

  @Test
  void testNullFail() {
    GeodesicIIR1 geodesicIIR1 = new GeodesicIIR1(RnGroup.INSTANCE, RealScalar.of(0.2));
    assertThrows(Exception.class, () -> geodesicIIR1.apply(null));
  }

  @Test
  void testZeroFail() {
    assertThrows(Exception.class, () -> new GeodesicIIR1(RnGroup.INSTANCE, RealScalar.of(0)));
  }

  @Test
  void testLargeFail() {
    assertThrows(Exception.class, () -> new GeodesicIIR1(RnGroup.INSTANCE, RealScalar.of(1.01)));
  }
}
