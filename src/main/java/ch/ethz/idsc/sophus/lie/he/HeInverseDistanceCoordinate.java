// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.BiinvariantInverseDistanceCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Flatten;

/** HeInverseDistanceCoordinate is invariant under left-action */
public enum HeInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      HeGroup.INSTANCE, //
      HeInverseDistanceCoordinate::flattenLog, //
      InverseNorm.of(HeAdNorm.INSTANCE));
  public static final BarycentricCoordinate BIINVAR = new BiinvariantInverseDistanceCoordinate( //
      HeGroup.INSTANCE, //
      HeInverseDistanceCoordinate::flattenLog);

  private static Tensor flattenLog(Tensor xyz) {
    return Flatten.of(HeExponential.INSTANCE.log(xyz));
  }
}
