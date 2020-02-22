// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se3InverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      Se3Group.INSTANCE, //
      Se3InverseDistanceCoordinate::flattenLog, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = new LieInverseDistanceCoordinate( //
      Se3Group.INSTANCE, //
      Se3InverseDistanceCoordinate::flattenLog, //
      InverseNorm.of(RnNormSquared.INSTANCE));

  private static Tensor flattenLog(Tensor m4x4) {
    return Flatten.of(Se3Exponential.INSTANCE.log(m4x4));
  }
}
