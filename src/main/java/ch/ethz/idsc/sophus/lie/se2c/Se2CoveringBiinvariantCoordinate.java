// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.BiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is
 * 
 * linear reproduction
 * lagrange property
 * biinvariant
 * smooth */
public class Se2CoveringBiinvariantCoordinate extends BiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new Se2CoveringBiinvariantCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new Se2CoveringBiinvariantCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  private Se2CoveringBiinvariantCoordinate(TensorUnaryOperator target) {
    super(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log, target);
  }
}
