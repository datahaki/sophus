// code by jph
package test.wrap;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.sophus.math.Permute;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Range;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.lie.Permutations;
import ch.alpine.tensor.sca.Chop;

public record BiinvariantMeanQ(BiinvariantMean biinvariantMean, Chop chop) {
  public void check(Tensor sequence, Tensor weights) {
    AffineQ.INSTANCE.requireMember(weights);
    Tensor expect = biinvariantMean.mean(sequence, weights);
    for (Tensor perm : Permutations.of(Range.of(0, weights.length()))) {
      TensorUnaryOperator permute = Permute.of(perm);
      Tensor result = biinvariantMean.mean(permute.apply(sequence), permute.apply(weights));
      chop.requireClose(result, expect);
    }
  }
}
