// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is invariant under left-action */
public class Se2CoveringInverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new Se2CoveringInverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new Se2CoveringInverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  private Se2CoveringInverseDistanceCoordinate(TensorUnaryOperator target) {
    super(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log, target);
  }
}
