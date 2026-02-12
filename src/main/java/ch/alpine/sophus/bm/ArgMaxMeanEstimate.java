// code by jph
package ch.alpine.sophus.bm;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.ArgMax;

/** serves as initial guess at begin of fix point iteration that
 * converges to exact mean.
 * 
 * @see IterativeBiinvariantMean */
public enum ArgMaxMeanEstimate implements MeanEstimate {
  INSTANCE;

  @Override // from MeanEstimate
  public Tensor estimate(Tensor sequence, Tensor weights) {
    return sequence.get(ArgMax.of(weights));
  }
}
