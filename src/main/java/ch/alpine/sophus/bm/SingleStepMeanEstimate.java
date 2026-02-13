// code by jph
package ch.alpine.sophus.bm;

import java.io.Serializable;

import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.spd.SpdPhongMean;
import ch.alpine.tensor.Tensor;

/** approximation of biinvariant mean using a geodesic average that has a
 * nested structure based on the weights.
 * 
 * because the result is not invariant under reordering of the input, the
 * implementation should only be used as an initial guess for the iterative
 * fixed point method.
 * 
 * tests have shown empirically for the SPD manifold, that the reducing
 * mean is closer to the exact mean than the {@link SpdPhongMean}
 * 
 * For R^n the ReducingMean coincides with the weighted average.
 * 
 * @param geodesicSpace
 * 
 * @see IterativeBiinvariantMean */
public record SingleStepMeanEstimate(Manifold manifold) implements MeanEstimate, Serializable {
  @Override
  public Tensor estimate(Tensor sequence, Tensor weights) {
    MeanEstimate meanEstimate = ArgMaxMeanEstimate.INSTANCE;
    Tensor shifted = meanEstimate.estimate(sequence, weights); // initial guess
    return MeanDefect.of(sequence, weights, manifold.exponential(shifted)).shifted();
  }
}
