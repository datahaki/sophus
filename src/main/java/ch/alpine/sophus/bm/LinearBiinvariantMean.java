// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.ext.Integers;

/** @see AffineQ */
public enum LinearBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Integers.requirePositive(weights.length());
    return VectorQ.require(weights).dot(sequence);
  }
}
