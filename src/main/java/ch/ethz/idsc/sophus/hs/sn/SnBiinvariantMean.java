// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.BiinvariantMean;
import ch.ethz.idsc.sophus.hs.IterativeBiinvariantMean;
import ch.ethz.idsc.tensor.sca.Chop;

/** Buss and Fillmore prove for data on spheres S^2, the step size of 1 for
 * weights w_i == 1/N is sufficient for convergence to the mean.
 * 
 * Reference:
 * "Spherical averages and applications to spherical splines and interpolation"
 * by S. R. Buss, J. P. Fillmore, 2001 */
public final class SnBiinvariantMean extends IterativeBiinvariantMean {
  public static final BiinvariantMean INSTANCE = new SnBiinvariantMean(Chop._14);

  /** @param chop
   * @return */
  public static BiinvariantMean of(Chop chop) {
    return new SnBiinvariantMean(chop);
  }

  /***************************************************/
  private SnBiinvariantMean(Chop chop) {
    super(SnManifold.INSTANCE, SnPhongMean.INSTANCE, chop);
  }
}
