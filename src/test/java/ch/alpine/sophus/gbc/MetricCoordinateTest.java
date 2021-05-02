// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.math.Genesis;
import ch.alpine.sophus.usr.AssertFail;
import junit.framework.TestCase;

public class MetricCoordinateTest extends TestCase {
  public void testCustomNullFail() {
    AssertFail.of(() -> MetricCoordinate.of((Genesis) null));
  }
}
