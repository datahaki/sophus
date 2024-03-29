// code by ob, jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.bm.ScalarBiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Careful:
 * So2FilterBiinvariantMean is not strictly a biinvariant mean, because the
 * computation is not invariant under permutation of input points and weights
 * for sequences of length 3 or greater. */
public enum So2FilterBiinvariantMean implements ScalarBiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Scalar mean(Tensor sequence, Tensor weights) {
    // sequences of odd and even length are permitted
    int middle = sequence.length() / 2;
    Scalar a0 = sequence.Get(middle);
    return So2.MOD.apply(a0.subtract(weights.dot(sequence.map(a0::subtract).map(So2.MOD))));
  }
}
