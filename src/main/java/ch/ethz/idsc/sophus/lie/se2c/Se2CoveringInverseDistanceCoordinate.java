// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean
 * 
 * Se2CoveringInverseDistanceCoordinate is invariant under left-action */
public class Se2CoveringInverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final ProjectedCoordinate INSTANCE = //
      new Se2CoveringInverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = //
      new Se2CoveringInverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));
  public static final ProjectedCoordinate AD_INVAR = //
      new Se2CoveringInverseDistanceCoordinate(InverseNorm.of(new Se2CoveringTarget(RnNormSquared.INSTANCE, RealScalar.ONE)));

  private Se2CoveringInverseDistanceCoordinate(TensorUnaryOperator target) {
    super(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log, target);
  }
}
