// code by jph
package ch.alpine.sophus.gbc.d2;

import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.crv.d2.Polygons;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.lie.rn.RnExponential;
import ch.alpine.sophus.math.Genesis;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
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
