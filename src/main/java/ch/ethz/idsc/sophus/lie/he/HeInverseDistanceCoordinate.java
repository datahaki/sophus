// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** HeInverseDistanceCoordinate is invariant under left-action */
public class HeInverseDistanceCoordinate extends LieBarycentricCoordinate {
  public static final BarycentricCoordinate INSTANCE = //
      new HeInverseDistanceCoordinate(InverseNorm.of(new HeTarget(RnNorm.INSTANCE, RealScalar.ONE)));

  private HeInverseDistanceCoordinate(TensorUnaryOperator target) {
    super(HeGroup.INSTANCE, HeExponential::flattenLog, target);
  }
}
