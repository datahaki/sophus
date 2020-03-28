// code by ob, jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import ch.ethz.idsc.sophus.hs.BiinvariantMeanDefect;
import ch.ethz.idsc.sophus.hs.MeanDefect;
import ch.ethz.idsc.sophus.math.AffineQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.sca.Chop;

/** Barycentric Fixed Point Iteration
 * 
 * Calculates the Biinvariant mean of a sequence in any Lie group by iterating over the implicit
 * formulation of the barycentric equation up until a proximity condition is fulfilled
 * 
 * The reference includes a factor 1/N (perhaps to prevent oscillations?),
 * but we don't see the need to reduce the step size in the iteration.
 * 
 * Reference:
 * "Exponential Barycenters of the Canonical Cartan Connection and Invariant Means on Lie Groups"
 * by Xavier Pennec, Vincent Arsigny, p.19-21, 2012 */
// TODO unify with SnMean and SpdMean ! also LieMidpoint
public class BiinvariantMeanImplicit implements Serializable {
  private static final int MAX_ITERATIONS = 100;
  private static final Chop CHOP = Chop._12;
  // ---
  private final LieGroup lieGroup;
  private final LieExponential lieExponential;
  private final MeanDefect meanDefect;

  /** @param lieGroup
   * @param lieExponential */
  public BiinvariantMeanImplicit(LieGroup lieGroup, LieExponential lieExponential) {
    this.lieGroup = Objects.requireNonNull(lieGroup);
    this.lieExponential = Objects.requireNonNull(lieExponential);
    meanDefect = BiinvariantMeanDefect.of(LieFlattenLogManifold.of(lieGroup, lieExponential::log));
  }

  /** @param sequence
   * @param weights
   * @return approximate biinvariant mean */
  public Optional<Tensor> apply(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    Tensor mean = sequence.get(ArgMax.of(weights)); // initial guess
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor next = update(sequence, weights, mean);
      if (CHOP.allZero(lieExponential.log(lieGroup.element(next).inverse().combine(mean))))
        return Optional.of(next);
      mean = next;
    }
    return Optional.empty();
  }

  private Tensor update(Tensor sequence, Tensor weights, Tensor mean) {
    return lieGroup.element(mean).combine( //
        lieExponential.exp(meanDefect.defect(sequence, weights, mean)));
  }
}
