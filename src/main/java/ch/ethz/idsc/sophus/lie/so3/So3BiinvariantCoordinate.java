// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

public class So3BiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = new So3BiinvariantCoordinate(RnNorm.INSTANCE);
  public static final BarycentricCoordinate SQUARED = new So3BiinvariantCoordinate(RnNormSquared.INSTANCE);

  public So3BiinvariantCoordinate(TensorNorm tensorNorm) {
    super(So3Group.INSTANCE, So3Exponential.INSTANCE::log, tensorNorm);
  }
}
