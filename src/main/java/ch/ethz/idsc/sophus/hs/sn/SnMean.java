// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Normalize;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;

/** Buss and Fillmore prove for data on spheres S^2, the step size of 1 for
 * weights w_i == 1/N is sufficient for convergence to the mean.
 * 
 * Reference:
 * "Spherical averages and applications to spherical splines and interpolation"
 * by S. R. Buss, J. P. Fillmore, 2001 */
public class SnMean implements BiinvariantMean {
  private static final int MAX_ITERATIONS = 100;
  private static final TensorUnaryOperator NORMALIZE = Normalize.with(Norm._2);
  /** high-precision for testing */
  public static final SnMean INSTANCE = new SnMean(Chop._14);
  /***************************************************/
  private final Chop chop;

  public SnMean(Chop chop) {
    this.chop = chop;
  }

  @Override // from BiinvariantMean
  public Tensor mean(Tensor sequence, Tensor weights) {
    Tensor mean = SnPhongMean.INSTANCE.mean(sequence, weights); // initial guess
    int count = 0;
    while (++count < MAX_ITERATIONS) {
      Tensor log = SnMeanDefect.INSTANCE.defect(sequence, weights, mean);
      if (chop.allZero(Norm._2.ofVector(log)))
        return NORMALIZE.apply(mean);
      mean = new SnExp(mean).exp(log);
      // normalization for numerical stability
      // if (count % 10 == 0)
      mean = NORMALIZE.apply(mean);
    }
    // TensorRuntimeException.of(sequence, weights).printStackTrace();
    // throw new RuntimeException("iteration limit reached");
    return NORMALIZE.apply(mean);
  }
}
