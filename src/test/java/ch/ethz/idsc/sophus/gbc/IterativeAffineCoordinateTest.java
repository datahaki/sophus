// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.r2.ConvexHull;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.red.Total;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class IterativeAffineCoordinateTest extends TestCase {
  private static void _check(Tensor levers, Tensor weights) {
    Chop._10.requireAllZero(weights.dot(levers));
    Chop._10.requireClose(Total.ofVector(weights), RealScalar.ONE);
  }

  public void testExp() {
    Genesis genesis = new IterativeAffineCoordinate(Amplifiers.exp(5), 10);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers)) {
        _check(levers, AffineCoordinate.INSTANCE.origin(levers));
        _check(levers, genesis.origin(levers));
      }
    }
  }

  public void testRamp() {
    Genesis genesis = new IterativeAffineCoordinate(Amplifiers.ramp(5), 10);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      if (ConvexHull.isInside(levers)) {
        _check(levers, AffineCoordinate.INSTANCE.origin(levers));
        _check(levers, genesis.origin(levers));
      }
    }
  }
}
