// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

public class HeBiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = new HeBiinvariantCoordinate(RnNorm.INSTANCE);
  public static final BarycentricCoordinate SQUARED = new HeBiinvariantCoordinate(RnNormSquared.INSTANCE);

  public HeBiinvariantCoordinate(TensorNorm tensorNorm) {
    super(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog, tensorNorm);
  }
}
