// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class StBiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new StBiinvariantCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new StBiinvariantCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  public StBiinvariantCoordinate(TensorUnaryOperator target) {
    super(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog, target);
  }
}
