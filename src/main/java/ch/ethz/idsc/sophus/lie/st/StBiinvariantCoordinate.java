// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

public class StBiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = new StBiinvariantCoordinate(RnNorm.INSTANCE);
  public static final BarycentricCoordinate SQUARED = new StBiinvariantCoordinate(RnNormSquared.INSTANCE);

  public StBiinvariantCoordinate(TensorNorm tensorNorm) {
    super(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog, tensorNorm);
  }
}
