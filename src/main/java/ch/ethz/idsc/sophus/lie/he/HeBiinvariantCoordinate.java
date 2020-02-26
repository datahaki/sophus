// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public class HeBiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new HeBiinvariantCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new HeBiinvariantCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  public HeBiinvariantCoordinate(TensorUnaryOperator target) {
    super(HeGroup.INSTANCE, HeExponential.INSTANCE::flattenLog, target);
  }
}
