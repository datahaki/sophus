// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.bm.BiinvariantMean;
import ch.alpine.sophus.bm.IterativeBiinvariantMean;
import ch.alpine.tensor.sca.Chop;

/** Buss and Fillmore prove for data on spheres S^2, the step size of 1 for
 * weights w_i == 1/N is sufficient for convergence to the mean.
 * 
 * Reference:
 * "Spherical averages and applications to spherical splines and interpolation"
 * by S. R. Buss, J. P. Fillmore, 2001 */
public enum SnBiinvariantMean {
  ;
  public static final BiinvariantMean INSTANCE = of(Chop._14);

  public static BiinvariantMean of(Chop chop) {
    return IterativeBiinvariantMean.of(SnManifold.INSTANCE, chop, SnPhongMean.INSTANCE);
  }
}
