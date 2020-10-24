// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.sn.SnRandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSample;
import ch.ethz.idsc.sophus.math.sample.RandomSampleInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class AffineGenesisTest extends TestCase {
  public void testS1() {
    Genesis genesis = MetricCoordinate.affine();
    RandomSampleInterface randomSampleInterface = SnRandomSample.of(1);
    for (int n = 3; n < 10; ++n) {
      Tensor levers = RandomSample.of(randomSampleInterface, n);
      Tensor w1 = genesis.origin(levers);
      Tensor w2 = AffineGenesis.INSTANCE.origin(levers);
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
        Tensor w2 = AffineGenesis.INSTANCE.origin(levers);
        Chop._10.requireClose(w1, w2);
      }
  }
}
