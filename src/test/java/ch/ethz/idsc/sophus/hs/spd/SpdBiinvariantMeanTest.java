// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class SpdBiinvariantMeanTest extends TestCase {
  public void testSimple() {
    Distribution distribution = UniformDistribution.unit();
    for (int n = 2; n < 4; ++n)
      for (int count = 0; count < 5; ++count) {
        int fn = n;
        Tensor sequence = Tensors.vector(i -> TestHelper.generateSpd(fn), 9);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distribution, sequence.length()));
        Tensor mean = SpdBiinvariantMean.INSTANCE.mean(sequence, weights);
        MeanDefect meanDefect = BiinvariantMeanDefect.of(SpdManifold.INSTANCE);
        Chop._06.requireAllZero(meanDefect.defect(sequence, weights, mean));
      }
  }
}
