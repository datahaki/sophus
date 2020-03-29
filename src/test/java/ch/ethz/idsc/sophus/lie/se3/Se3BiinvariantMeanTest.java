// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import java.util.Arrays;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.sophus.hs.MeanDefect;
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
  private static final IterativeBiinvariantMean ITERATIVE_BIINVARIANT_MEAN = //
      IterativeBiinvariantMean.of(Se3Manifold.HS_EXP);
  public static final MeanDefect MEAN_DEFECT = BiinvariantMeanDefect.of(Se3Manifold.HS_EXP);

  public void testRandom() {
    Distribution distributiont = NormalDistribution.of(4, 1);
    for (int count = 0; count < 10; ++count)
      for (int n = 7; n < 13; ++n) {
        Tensor sequence = Tensors.vector(i -> TestHelper.spawn_Se3(), n);
        Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(distributiont, n));
        Tensor mean = ITERATIVE_BIINVARIANT_MEAN.mean(sequence, weights);
        assertEquals(Dimensions.of(mean), Arrays.asList(4, 4));
        Tensor defect = MEAN_DEFECT.defect(sequence, weights, mean);
        assertEquals(Dimensions.of(defect), Arrays.asList(2, 3));
        Chop._08.requireAllZero(defect);
      }
  }
}
