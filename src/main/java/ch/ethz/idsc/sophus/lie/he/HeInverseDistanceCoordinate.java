// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieAffineCoordinate;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinate;

/** HeInverseDistanceCoordinate is invariant under left-action */
public enum HeInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieInverseDistanceCoordinate( //
      HeGroup.INSTANCE, //
      HeExponential::flattenLog, //
      InverseNorm.of(HeAdNorm.INSTANCE));
  public static final BarycentricCoordinate BIINVAR = new LieAffineCoordinate( //
      HeGroup.INSTANCE, //
      HeExponential::flattenLog);
}
