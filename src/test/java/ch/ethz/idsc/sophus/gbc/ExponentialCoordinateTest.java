// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.lie.r2.ExponentialCoordinate;
import ch.ethz.idsc.sophus.math.var.InversePowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Round;
import junit.framework.TestCase;

public class ExponentialCoordinateTest extends TestCase {
  private static void _check(Genesis inner) {
    for (int k = 0; k < 10; ++k) {
      Genesis genesis = ExponentialCoordinate.of(inner, k);
      Distribution distribution = UniformDistribution.of(-1, 5);
      Tensor levers = RandomVariate.of(distribution, 10, 2);
      Tensor weights = genesis.origin(levers);
      Tensor defect = weights.dot(levers);
      if (!Chop._10.allZero(defect)) {
        System.out.println(defect.map(Round._3));
        Chop._10.requireAllZero(defect);
      }
    }
  }

  public void testSimple() {
    _check(AffineCoordinate.INSTANCE);
    _check(MetricCoordinate.of(InversePowerVariogram.of(2)));
  }
}
