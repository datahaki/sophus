// code by jph
package ch.alpine.sophus.flt.ga;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.tensor.RealScalar;

public class GeodesicIIRnTest {
  @Test
  public void testFailOpNull() {
    assertThrows(Exception.class, () -> GeodesicIIRn.of(null, RnGeodesic.INSTANCE, 3, RealScalar.ONE));
  }

  @Test
  public void testFailScalarNull() {
    assertThrows(Exception.class, () -> GeodesicIIRn.of(x -> x.get(0), RnGeodesic.INSTANCE, 3, null));
  }

  @Test
  public void testFailGeodesicNull() {
    assertThrows(Exception.class, () -> GeodesicIIRn.of(x -> x.get(0), null, 3, RealScalar.ONE));
  }
}
