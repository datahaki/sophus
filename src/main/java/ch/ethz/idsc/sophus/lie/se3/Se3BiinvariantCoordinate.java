// code by jph
package ch.ethz.idsc.sophus.lie.se3;

import ch.ethz.idsc.sophus.lie.LieBiinvariantCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** biinvariant generalized barycentric coordinates */
public class Se3BiinvariantCoordinate extends LieBiinvariantCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new Se3BiinvariantCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new Se3BiinvariantCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  public Se3BiinvariantCoordinate(TensorUnaryOperator target) {
    super(Se3Group.INSTANCE, Se3Exponential::flattenLog, target);
  }
}
