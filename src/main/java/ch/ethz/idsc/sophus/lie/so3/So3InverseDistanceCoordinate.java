// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** barycentric coordinates that are invariant under left-action, right-action and inversion */
public class So3InverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new So3InverseDistanceCoordinate(InverseNorm.of(RnNorm.INSTANCE));

  private So3InverseDistanceCoordinate(TensorUnaryOperator target) {
    super(So3Group.INSTANCE, So3Exponential.INSTANCE::log, target);
  }
}
