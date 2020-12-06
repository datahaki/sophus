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

public class IterativeTargetCoordinateTest extends TestCase {
  public void testSimple() {
    int count = 0;
    for (int n = 5; n < 20; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers)) {
        Genesis genesis = new IterativeTargetCoordinate(InverseDistanceWeighting.of(InversePowerVariogram.of(2)), 100);
        Tensor vector = genesis.origin(levers);
        AffineQ.require(vector, Chop._08);
        Chop._08.requireAllZero(vector.dot(levers));
        ++count;
      }
    }
    assertTrue(2 < count);
  }
}
