// code by jph
package ch.alpine.sophus.gbc.d2;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import ch.alpine.sophus.api.Genesis;
import ch.alpine.sophus.bm.MeanDefect;
import ch.alpine.sophus.crv.d2.OriginEnclosureQ;
import ch.alpine.sophus.gbc.MetricCoordinate;
import ch.alpine.sophus.lie.rn.RnExponential;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.lie.r2.CirclePoints;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;

class IterativeCoordinateMatrixTest {
  @Test
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-0.1, 0.1);
    for (int k = 1; k < 5; ++k) {
      Genesis genesis = new IterativeCoordinate(MetricCoordinate.affine(), k);
      for (int n = 3; n < 10; ++n) {
        Tensor levers = CirclePoints.of(n).add(RandomVariate.of(distribution, n, 2));
        Tensor weights = genesis.origin(levers);
        MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
    }
  }

  @Test
  public void testInside() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    int count = 0;
    for (int k = 1; k < 5; ++k) {
      Genesis genesis = new IterativeCoordinate(MetricCoordinate.affine(), k);
      for (int n = 3; n < 10; ++n) {
        Tensor levers = RandomVariate.of(distribution, n, 2);
        if (OriginEnclosureQ.INSTANCE.test(levers)) {
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
