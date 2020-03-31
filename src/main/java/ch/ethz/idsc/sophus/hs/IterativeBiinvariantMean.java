// code by ob, jph
package ch.ethz.idsc.sophus.hs;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import ch.ethz.idsc.sophus.math.Exponential;
import ch.ethz.idsc.tensor.Tensor;
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
public class IterativeBiinvariantMean implements BiinvariantMean, Serializable {
  private static final int MAX_ITERATIONS = 100;

  /** @param hsExponential
   * @return */
  public static IterativeBiinvariantMean of(HsExponential hsExponential) {
    return of(hsExponential, Chop._12);
  }

  /** @param hsExponential
   * @param chop
   * @return */
  public static IterativeBiinvariantMean of(HsExponential hsExponential, Chop chop) {
    return new IterativeBiinvariantMean(hsExponential, chop);
  }

  /***************************************************/
  private final HsExponential hsExponential;
  private final BiinvariantMean initialGuess;
  private final Chop chop;
  private final MeanDefect meanDefect;

  /** @param hsExponential
   * @param initialGuess
   * @param chop */
  protected IterativeBiinvariantMean(HsExponential hsExponential, BiinvariantMean initialGuess, Chop chop) {
    this.hsExponential = hsExponential;
    this.initialGuess = Objects.requireNonNull(initialGuess);
    this.chop = Objects.requireNonNull(chop);
    meanDefect = BiinvariantMeanDefect.of(hsExponential);
  }

  /** @param hsExponential
   * @param chop */
  protected IterativeBiinvariantMean(HsExponential hsExponential, Chop chop) {
    this(hsExponential, ArgMaxBiinvariantMean.INSTANCE, chop);
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    return apply(sequence, weights).get();
  }

  /** @param sequence
   * @param weights
   * @return approximate biinvariant mean */
  public final Optional<Tensor> apply(Tensor sequence, Tensor weights) {
    Tensor mean = initialGuess.mean(sequence, weights); // initial guess
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      Tensor next = hsExponential.exponential(mean).exp(meanDefect.defect(sequence, weights, mean));
      Exponential exponential = hsExponential.exponential(next);
      if (chop.allZero(exponential.log(mean)))
        return Optional.of(next);
      mean = next;
    }
    return Optional.empty();
  }
}
