// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.BiinvariantInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseBiNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;

/** given sequence and mean the implementation computes the weights that satisfy
 * 
 * Se2CoveringBiinvariantMean[sequence, weights] == mean */
public enum Se3InverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new BiinvariantInverseDistanceCoordinate( //
      Se3Group.INSTANCE, //
      Se3InverseDistanceCoordinate::flattenLog, //
      InverseBiNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate BIINVAR = new BiinvariantInverseDistanceCoordinate( //
      Se3Group.INSTANCE, //
      Se3InverseDistanceCoordinate::flattenLog, //
      InverseBiNorm.of(RnNorm.INSTANCE));

  private static Tensor flattenLog(Tensor m4x4) {
    return Flatten.of(Se3Exponential.INSTANCE.log(m4x4));
  }
}
