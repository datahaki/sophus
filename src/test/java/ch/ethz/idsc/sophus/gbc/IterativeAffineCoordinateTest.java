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
  public void testSimple() {
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(NormalDistribution.standard(), n, 2);
      Genesis genesis = new IterativeAffineCoordinate(10, RealScalar.of(5));
      if (ConvexHull.isInside(levers)) {
        Tensor w1 = AffineCoordinate.INSTANCE.origin(levers);
        Chop._10.requireAllZero(w1.dot(levers));
        Chop._10.requireClose(Total.ofVector(w1), RealScalar.ONE);
        Tensor w2 = genesis.origin(levers);
        Chop._10.requireAllZero(w2.dot(levers));
        Chop._10.requireClose(Total.ofVector(w2), RealScalar.ONE);
      }
    }
  }
}
