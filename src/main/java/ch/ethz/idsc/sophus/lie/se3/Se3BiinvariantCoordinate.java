// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

/** biinvariant generalized barycentric coordinates */
public class Se3BiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = new Se3BiinvariantCoordinate(RnNorm.INSTANCE);
  public static final BarycentricCoordinate SQUARED = new Se3BiinvariantCoordinate(RnNormSquared.INSTANCE);

  public Se3BiinvariantCoordinate(TensorNorm tensorNorm) {
    super(Se3Group.INSTANCE, Se3Exponential::flattenLog, tensorNorm);
  }
}
