// code by jph
package ch.ethz.idsc.sophus.ply.d2;

import ch.ethz.idsc.sophus.bm.MeanDefect;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.sophus.math.Genesis;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.lie.r2.CirclePoints;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class IterativeCoordinateMatrixTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int k = 1; k < 5; ++k) {
      Genesis genesis = IterativeCoordinate.of(MetricCoordinate.affine(), k);
      for (int n = 3; n < 10; ++n) {
        Tensor levers = CirclePoints.of(n).add(RandomVariate.of(distribution, n, 2));
        Tensor weights = genesis.origin(levers);
        MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
    }
  }

  public void testInside() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    int count = 0;
    for (int k = 1; k < 5; ++k) {
      Genesis genesis = IterativeCoordinate.of(MetricCoordinate.affine(), k);
      for (int n = 3; n < 10; ++n) {
        Tensor levers = RandomVariate.of(distribution, n, 2);
        if (Polygons.isInside(levers)) {
          Tensor weights = genesis.origin(levers);
          MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
          Tensor tangent = meanDefect.tangent();
          Chop._07.requireAllZero(tangent);
          ++count;
        }
      }
    }
    assertTrue(0 < count);
  }
}
