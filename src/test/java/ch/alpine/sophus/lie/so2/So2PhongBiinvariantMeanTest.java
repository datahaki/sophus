// code by jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.bm.BiinvariantMeanTestHelper;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.io.Primitives;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.nrm.NormalizeTotal;
import ch.alpine.tensor.pdf.Distribution;
import ch.alpine.tensor.pdf.RandomVariate;
import ch.alpine.tensor.pdf.c.UniformDistribution;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Clips;
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
