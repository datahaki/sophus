// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.krg.InverseDistanceWeighting;
import ch.ethz.idsc.sophus.lie.r2.ConvexHull;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.sophus.usr.AssertFail;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LagrangeCoordinateTest extends TestCase {
  private static void _check(Tensor levers, Tensor weights) {
    AffineQ.require(weights, Chop._10);
    Chop._08.requireAllZero(weights.dot(levers));
  }

  public void testSimple() {
    int count = 0;
    Genesis idw = InverseDistanceWeighting.of(InversePowerVariogram.of(2));
    Genesis genesis = LagrangeCoordinate.of(idw);
    Genesis idc = MetricCoordinate.of(idw);
    for (int n = 5; n < 20; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers)) {
        _check(levers, genesis.origin(levers));
        _check(levers, idc.origin(levers));
        ++count;
      }
    }
    assertTrue(2 < count);
  }

  public void testNullFail() {
    AssertFail.of(() -> LagrangeCoordinate.of(null));
  }
}
