// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.lie.BiinvariantCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * RnBiinvariantMean[sequence, weights] == mean
 * 
 * The RnInverseDistanceCoordinate is invariant under left-, right-translation and inversion. */
public class RnBiinvariantCoordinate extends BiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new RnBiinvariantCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new RnBiinvariantCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  private RnBiinvariantCoordinate(TensorUnaryOperator target) {
    super(RnGroup.INSTANCE, RnExponential.INSTANCE::log, target);
  }
}
