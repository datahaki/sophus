// code by ob, jph
package ch.alpine.sophus.bm;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.ArgMax;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.Chop;

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

  /** @param homogeneousSpace
   * @param chop
   * @param initialGuess
   * @return */
  public static IterativeBiinvariantMean of(HomogeneousSpace homogeneousSpace, Chop chop, BiinvariantMean initialGuess) {
    return new IterativeBiinvariantMean(homogeneousSpace, chop, initialGuess);
  }

  /** uses arg max of weights in sequence as initial guess
   * 
   * @param homogeneousSpace
   * @param chop
   * @return */
  public static IterativeBiinvariantMean argmax(HomogeneousSpace homogeneousSpace, Chop chop) {
    return of(homogeneousSpace, chop, ArgMaxSelection.INSTANCE);
  }

  public static IterativeBiinvariantMean argmax(HomogeneousSpace homogeneousSpace) {
    return argmax(homogeneousSpace, Tolerance.CHOP);
  }

  /** @param homogeneousSpace
   * @param chop
   * @return */
  public static IterativeBiinvariantMean reduce(HomogeneousSpace homogeneousSpace, Chop chop) {
    return new IterativeBiinvariantMean(homogeneousSpace, chop, new ReducingMean(homogeneousSpace));
  }

  /** serves as initial guess at begin of fix point iteration that
   * converges to exact mean.
   * 
   * @see IterativeBiinvariantMean */
  private enum ArgMaxSelection implements BiinvariantMean {
    INSTANCE;

    @Override // from BiinvariantMean
    public Tensor mean(Tensor sequence, Tensor weights) {
      return sequence.get(ArgMax.of(weights));
    }
  }

  // ---
  private final HomogeneousSpace homogeneousSpace;
  private final Chop chop;
  private final BiinvariantMean initialGuess;

  private IterativeBiinvariantMean(HomogeneousSpace homogeneousSpace, Chop chop, BiinvariantMean initialGuess) {
    this.homogeneousSpace = homogeneousSpace;
    this.chop = Objects.requireNonNull(chop);
    this.initialGuess = Objects.requireNonNull(initialGuess);
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.INSTANCE.requireMember(weights);
    return apply(sequence, weights).orElseThrow();
  }

  /** @param sequence
   * @param weights
   * @return approximate biinvariant mean, or empty if convergence fail */
  public final Optional<Tensor> apply(Tensor sequence, Tensor weights) {
    Tensor shifted = initialGuess.mean(sequence, weights); // initial guess
    for (int iteration = 0; iteration < MAX_ITERATIONS; ++iteration) {
      MeanDefect meanDefect = MeanDefect.of(sequence, weights, homogeneousSpace.exponential(shifted));
      shifted = meanDefect.shifted();
      if (chop.allZero(meanDefect.tangent()))
        return Optional.of(shifted);
    }
    return Optional.empty();
  }
}
