// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;

/** StInverseDistanceCoordinate is invariant under left-action */
public enum StInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      StGroup.INSTANCE, //
      StInverseDistanceCoordinate::flattenLog, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = new LieInverseDistanceCoordinate( //
      StGroup.INSTANCE, //
      StInverseDistanceCoordinate::flattenLog, //
      InverseNorm.of(RnNormSquared.INSTANCE));

  private static Tensor flattenLog(Tensor lambda_t) {
    return Flatten.of(StExponential.INSTANCE.log(lambda_t));
  }
}
