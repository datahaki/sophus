// code by jph
package ch.alpine.sophus.flt.ga;

import ch.alpine.sophus.lie.rn.RnGeodesic;
import ch.alpine.sophus.usr.AssertFail;
import ch.alpine.tensor.RealScalar;
import junit.framework.TestCase;

public class GeodesicIIRnTest extends TestCase {
  public void testFailOpNull() {
    AssertFail.of(() -> GeodesicIIRn.of(null, RnGeodesic.INSTANCE, 3, RealScalar.ONE));
  }

  public void testFailScalarNull() {
    AssertFail.of(() -> GeodesicIIRn.of(x -> x.get(0), RnGeodesic.INSTANCE, 3, null));
  }

  public void testFailGeodesicNull() {
    AssertFail.of(() -> GeodesicIIRn.of(x -> x.get(0), null, 3, RealScalar.ONE));
  }
}
