// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public class Se3InverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new Se3InverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new Se3InverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  private Se3InverseDistanceCoordinate(TensorUnaryOperator target) {
    super(Se3Group.INSTANCE, Se3Exponential::flattenLog, target);
  }
}
