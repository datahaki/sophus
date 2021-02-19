// code by jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanTestHelper;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Range;
import ch.ethz.idsc.tensor.io.Primitives;
import ch.ethz.idsc.tensor.lie.Permutations;
import ch.ethz.idsc.tensor.nrm.NormalizeTotal;
import ch.ethz.idsc.tensor.pdf.Distribution;
import ch.ethz.idsc.tensor.pdf.RandomVariate;
import ch.ethz.idsc.tensor.pdf.UniformDistribution;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Clips;
import junit.framework.TestCase;

public class So2PhongBiinvariantMeanTest extends TestCase {
  public void testPermutations() {
    Distribution distribution = UniformDistribution.of(Clips.absolute(Math.PI));
    for (int length = 1; length < 6; ++length) {
      // here, we hope that no antipodal points are generated
      Tensor sequence = RandomVariate.of(distribution, length);
      Tensor weights = NormalizeTotal.FUNCTION.apply(RandomVariate.of(UniformDistribution.unit(), length));
      Scalar solution = So2PhongBiinvariantMean.INSTANCE.mean(sequence, weights);
      for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
        int[] index = Primitives.toIntArray(perm);
        Tensor result = So2PhongBiinvariantMean.INSTANCE.mean(BiinvariantMeanTestHelper.order(sequence, index),
            BiinvariantMeanTestHelper.order(weights, index));
        Chop._12.requireClose(result, solution);
      }
    }
  }
}
