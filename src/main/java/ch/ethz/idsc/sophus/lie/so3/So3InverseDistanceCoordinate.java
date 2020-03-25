// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** barycentric coordinates that are invariant under left-action, right-action and inversion */
public class So3InverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final ProjectedCoordinate INSTANCE = //
      new So3InverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));
  public static final ProjectedCoordinate SQUARED = //
      new So3InverseDistanceCoordinate(InverseNorm.of(RnNormSquared.INSTANCE));

  public So3InverseDistanceCoordinate(TensorUnaryOperator target) {
    super(So3Group.INSTANCE, So3Exponential.INSTANCE::log, target);
  }
}
