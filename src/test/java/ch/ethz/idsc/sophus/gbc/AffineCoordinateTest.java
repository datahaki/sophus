// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.lie.r2.Polygons;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Exp;
import junit.framework.TestCase;

public class AffineCoordinateTest extends TestCase {
  public void testS1() {
    Genesis genesis = MetricCoordinate.affine();
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(1);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomSample.of(randomSampleInterface, n);
      Tensor w1 = genesis.origin(levers);
      Tensor w2 = AffineCoordinate.INSTANCE.origin(levers);
      Chop._10.requireClose(w1, w2);
    }
  }

  public void testRn() {
    Genesis genesis = MetricCoordinate.affine();
    Distribution distribution = NormalDistribution.standard();
    for (int n = 3; n < 10; ++n)
      for (int k = 0; k < 2; ++k) {
        int d = 2 + k;
        Tensor levers = RandomVariate.of(distribution, n + k, d);
        Tensor w1 = genesis.origin(levers);
        Tensor w2 = AffineCoordinate.INSTANCE.origin(levers);
        Chop._10.requireClose(w1, w2);
      }
  }

  public void testIterateR2() {
    Distribution distribution = UniformDistribution.of(-1, 5);
    int d = 2;
    for (int n = 4; n < 10; ++n)
      for (int count = 0; count < 10; ++count) {
        Tensor levers = RandomVariate.of(distribution, n, d);
        Tensor origin = levers.copy();
        if (Polygons.isInside(levers)) {
          for (int i = 0; i < 3; ++i) {
            Tensor weights = AffineCoordinate.INSTANCE.origin(levers);
            weights = NormalizeTotal.FUNCTION.apply(weights.map(Exp.FUNCTION));
            levers = weights.pmul(levers);
            System.out.println(weights.dot(origin));
          }
        }
      }
  }
}
