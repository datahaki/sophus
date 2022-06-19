// code by ob, jph
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.bm.ScalarBiinvariantMean;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Hint:
 * angles are required to lie on a half-circle which is not necessarily centered at the origin
 * 
 * Reference:
 * https://hal.inria.fr/inria-00073318/
 * by Xavier Pennec */
public enum So2LinearBiinvariantMean implements ScalarBiinvariantMean {
  INSTANCE;

  @Override // from ScalarBiinvariantMean
  public Scalar mean(Tensor sequence, Tensor weights) {
    Scalar a0 = sequence.Get(0);
    return So2.MOD.apply(a0.subtract(weights.dot(StaticHelper.rangeQ(sequence.map(a0::subtract).map(So2.MOD)))));
  }
}
