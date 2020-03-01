// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.BiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.TensorNorm;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is
 * 
 * linear reproduction
 * lagrange property
 * biinvariant
 * smooth for beta == 2 */
public class Se2CoveringBiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BiinvariantCoordinate INSTANCE = new Se2CoveringBiinvariantCoordinate(RnNorm.INSTANCE);
  public static final BiinvariantCoordinate SQUARED = new Se2CoveringBiinvariantCoordinate(RnNormSquared.INSTANCE);

  private Se2CoveringBiinvariantCoordinate(TensorNorm tensorNorm) {
    super(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log, tensorNorm);
  }
}
