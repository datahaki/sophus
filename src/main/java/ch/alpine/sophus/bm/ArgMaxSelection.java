// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.red.ArgMax;

/** serves as initial guess at begin of fix point iteration that
 * converges to exact mean.
 * 
 * @see IterativeBiinvariantMean */
/* package */ enum ArgMaxSelection implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return sequence.get(ArgMax.of(AffineQ.require(weights)));
  }
}
