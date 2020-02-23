// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;

/** StInverseDistanceCoordinate is invariant under left-action */
public enum StInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieBarycentricCoordinate( //
      StGroup.INSTANCE, //
      StExponential::flattenLog, //
      InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = new LieBarycentricCoordinate( //
      StGroup.INSTANCE, //
      StExponential::flattenLog, //
      InverseNorm.of(RnNormSquared.INSTANCE));
}
