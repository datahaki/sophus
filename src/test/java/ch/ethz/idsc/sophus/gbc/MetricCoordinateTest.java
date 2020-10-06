// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class MetricCoordinateTest extends TestCase {
  public void testCustomNullFail() {
    AssertFail.of(() -> MetricCoordinate.custom(null));
  }
}
