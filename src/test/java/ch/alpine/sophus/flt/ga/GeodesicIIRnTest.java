// code by jph
package ch.alpine.sophus.flt.ga;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;

public class GeodesicIIRnTest {
  @Test
  public void testFailOpNull() {
    AssertFail.of(() -> GeodesicIIRn.of(null, RnGeodesic.INSTANCE, 3, RealScalar.ONE));
  }

  @Test
  public void testFailScalarNull() {
    AssertFail.of(() -> GeodesicIIRn.of(x -> x.get(0), RnGeodesic.INSTANCE, 3, null));
  }

  @Test
  public void testFailGeodesicNull() {
    AssertFail.of(() -> GeodesicIIRn.of(x -> x.get(0), null, 3, RealScalar.ONE));
  }
}
