// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.sca.Chop;

/** serves as initial guess at begin of fix point iteration that
 * converges to exact mean.
 * 
 * @see IterativeBiinvariantMean */
/* package */ enum ArgMaxBiinvariantMean implements BiinvariantMean {
  INSTANCE;

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return sequence.get(ArgMax.of(AffineQ.require(weights, Chop._08)));
  }
}
