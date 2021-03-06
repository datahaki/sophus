// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SnGlobalBiinvariantMeanTest extends TestCase {
  public void testSimple() {
    Distribution distribution = NormalDistribution.standard();
    for (int dim = 2; dim < 5; ++dim)
      for (int length = 1; length < 10; ++length) {
        Tensor sequence = Tensor.of(RandomVariate.of(distribution, length, dim).stream().map(Vector2Norm.NORMALIZE));
        Tensor mean = SnGlobalBiinvariantMean.INSTANCE.mean(sequence, RandomVariate.of(distribution, length));
        Chop._10.requireClose(mean, Vector2Norm.NORMALIZE.apply(mean));
      }
  }
}
