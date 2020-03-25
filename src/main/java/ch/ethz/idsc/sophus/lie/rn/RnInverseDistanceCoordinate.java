// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieBarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * RnBiinvariantMean[sequence, weights] == mean
 * 
 * The RnInverseDistanceCoordinate is invariant under left-, right-translation and inversion. */
public class RnInverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final ProjectedCoordinate INSTANCE = //
      new RnInverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = //
      new RnInverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  private RnInverseDistanceCoordinate(TensorUnaryOperator target) {
    super(RnGroup.INSTANCE, RnExponential.INSTANCE::log, target);
  }
}
