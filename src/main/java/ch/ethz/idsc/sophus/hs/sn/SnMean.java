// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.lie.BiinvariantMean;
import ch.ethz.idsc.sophus.lie.MeanDefect;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.ArgMax;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;

/** Buss and Fillmore prove for data on spheres S^2, the step size of 1 for
 * weights w_i == 1/N is sufficient for convergence to the mean.
 * 
 * Reference:
 * "Spherical averages and applications to spherical splines and interpolation"
 * by S. R. Buss, J. P. Fillmore, 2001 */
public enum SnMean implements BiinvariantMean, MeanDefect {
  INSTANCE;

  private static final int MAX_ITERATIONS = 100;
  private static final Chop CHOP = Chop._14;
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = sequence.get(ArgMax.of(weights)); // initial guess
    int count = 0;
    while (++count < MAX_ITERATIONS) {
      Tensor log = defect(sequence, weights, mean);
      if (CHOP.allZero(Norm._2.ofVector(log)))
        return mean;
      mean = new SnExp(mean).exp(log);
      // normalization for numerical stability
      mean = NORMALIZE.apply(mean);
    }
    throw TensorRuntimeException.of(sequence, weights);
  }

  /** @param sequence
   * @param weights vector affine
   * @param candidate
   * @return */
  @Override
  public Tensor defect(Tensor sequence, Tensor weights, Tensor candidate) {
    return weights.dot(Tensor.of(sequence.stream().map(new SnExp(candidate)::log)));
  }
}
