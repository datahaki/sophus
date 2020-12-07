// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.krg.InverseDistanceWeighting;
import ch.ethz.idsc.sophus.lie.r2.ConvexHull;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class LagrangeCoordinateTest extends TestCase {
  public void testSimple() {
    int count = 0;
    Genesis idw = InverseDistanceWeighting.of(InversePowerVariogram.of(2));
    LagrangeCoordinate genesis = new LagrangeCoordinate(idw);
    Genesis idc = MetricCoordinate.of(idw);
    for (int n = 5; n < 20; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers)) {
        Tensor v1 = genesis.origin(levers);
        AffineQ.require(v1, Chop._10);
        Chop._08.requireAllZero(v1.dot(levers));
        ++count;
        idc.origin(levers);
      }
    }
    assertTrue(2 < count);
  }
}
