// code by ob, jph
package ch.ethz.idsc.sophus.lie.so2;

import ch.ethz.idsc.sophus.lie.ScalarBiinvariantMean;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;

/** Hint:
 * angles are required to lie on a half-circle which is not necessarily centered at the origin
 * 
 * Reference:
 * https://hal.inria.fr/inria-00073318/
 * by Xavier Pennec */
public enum So2LinearBiinvariantMean implements ScalarBiinvariantMean {
  INSTANCE;
  // ---
  @Override // from ScalarBiinvariantMean
  public Scalar mean(Tensor sequence, Tensor weights) {
    Scalar a0 = sequence.Get(0);
    return So2.MOD.apply(a0.subtract(weights.dot(So2Helper.rangeQ(sequence.map(a0::subtract).map(So2.MOD)))));
  }
}
