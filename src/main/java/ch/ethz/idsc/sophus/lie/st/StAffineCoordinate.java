// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.LieAffineCoordinate;

public enum StAffineCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieAffineCoordinate( //
      StGroup.INSTANCE, //
      StExponential::flattenLog);
}
