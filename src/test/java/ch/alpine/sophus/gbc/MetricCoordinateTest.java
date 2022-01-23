// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class MetricCoordinateTest extends TestCase {
  public void testCustomNullFail() {
    AssertFail.of(() -> new MetricCoordinate(null));
  }
}
