// code by ob, jph
package ch.alpine.sophus.bm;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;

import ch.alpine.sophus.api.GeodesicSpace;
import ch.alpine.sophus.hs.HomogeneousSpace;
import ch.alpine.sophus.math.AffineQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.ArgMax;
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

  /** @param hsManifold
   * @param chop
   * @param initialGuess
   * @return */
  public static IterativeBiinvariantMean of(HomogeneousSpace hsManifold, Chop chop, BiinvariantMean initialGuess) {
    return new IterativeBiinvariantMean(hsManifold, chop, initialGuess);
  }

  /** uses arg max of weights in sequence as initial guess
   * 
   * @param hsManifold
   * @param chop
   * @return */
  public static IterativeBiinvariantMean of(HomogeneousSpace hsManifold, Chop chop) {
    return of(hsManifold, chop, ArgMaxSelection.INSTANCE);
  }

  /** @param hsManifold
   * @param chop
   * @param geodesicSpace
   * @return */
  public static IterativeBiinvariantMean of(HomogeneousSpace hsManifold, Chop chop, GeodesicSpace geodesicSpace) {
    return new IterativeBiinvariantMean(hsManifold, chop, ReducingMean.of(geodesicSpace));
  }

  /** serves as initial guess at begin of fix point iteration that
   * converges to exact mean.
   * 
   * @see IterativeBiinvariantMean */
  private static enum ArgMaxSelection implements BiinvariantMean {
    INSTANCE;

    @Override // from BiinvariantMean
    public Tensor mean(Tensor sequence, Tensor weights) {
      return sequence.get(ArgMax.of(AffineQ.require(weights)));
    }
  }

  // ---
  private final HomogeneousSpace hsManifold;
  private final Chop chop;
  private final BiinvariantMean initialGuess;

  private IterativeBiinvariantMean(HomogeneousSpace hsManifold, Chop chop, BiinvariantMean initialGuess) {
    this.hsManifold = hsManifold;
    this.chop = Objects.requireNonNull(chop);
    this.initialGuess = Objects.requireNonNull(initialGuess);
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    AffineQ.require(weights);
    return apply(sequence, weights).orElseThrow();
  }

  /** @param sequence
   * @param weights
   * @return approximate biinvariant mean */
  public final Optional<Tensor> apply(Tensor sequence, Tensor weights) {
    Tensor shifted = initialGuess.mean(sequence, weights); // initial guess
    for (int count = 0; count < MAX_ITERATIONS; ++count) {
      MeanDefect meanDefect = new MeanDefect(sequence, weights, hsManifold.exponential(shifted));
      shifted = meanDefect.shifted();
      if (chop.allZero(meanDefect.tangent()))
        return Optional.of(shifted);
    }
    return Optional.empty();
  }
}
