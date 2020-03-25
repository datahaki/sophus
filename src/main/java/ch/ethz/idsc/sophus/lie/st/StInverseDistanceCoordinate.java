// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** StInverseDistanceCoordinate is invariant under left-action */
public class StInverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final ProjectedCoordinate INSTANCE = //
      new StInverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = //
      new StInverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  private StInverseDistanceCoordinate(TensorUnaryOperator target) {
    super(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog, target);
  }
}
