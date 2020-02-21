// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.util.Arrays;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Dimensions;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.NormalDistribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.sca.Chop;
import junit.framework.TestCase;

public class Se3BiinvariantMeanTest extends TestCase {
  public void testRandom() {
    Distribution distributiont = NormalDistribution.of(4, 1);
    for (int count = 0; count < 10; ++count)
      for (int n = 7; n < 13; ++n) {
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distributiont, n));
        Tensor mean = Se3BiinvariantMean.INSTANCE.mean(sequence, weights);
        assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
        Tensor defect = Se3BiinvariantMeanDefect.INSTANCE.defect(sequence, weights, mean);
        assertEquals(Dimensions.of(defect), Arrays.asList(2, 3));
        Chop._08.requireAllZero(defect);
      }
  }
}
