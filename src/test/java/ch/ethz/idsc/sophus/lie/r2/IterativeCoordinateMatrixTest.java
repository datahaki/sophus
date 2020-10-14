// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import ch.ethz.idsc.sophus.gbc.Genesis;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.lie.rn.RnExponential;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class IterativeCoordinateMatrixTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.of(-10, 10);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomVariate.of(distribution, n, 2);
      for (int k = 0; k < 5; ++k) {
        Genesis genesis = IterativeCoordinate.of(MetricCoordinate.affine(), k);
        Tensor weights = genesis.origin(levers);
        MeanDefect meanDefect = new MeanDefect(levers, weights, RnExponential.INSTANCE);
        Tensor tangent = meanDefect.tangent();
        Chop._07.requireAllZero(tangent);
      }
    }
  }
}
