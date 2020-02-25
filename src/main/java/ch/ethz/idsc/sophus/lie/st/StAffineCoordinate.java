// code by jph
package ch.ethz.idsc.sophus.lie.st;

import ch.ethz.idsc.sophus.lie.LieAffineCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;

public class StAffineCoordinate extends LieAffineCoordinate {
  public static final BarycentricCoordinate INSTANCE = new StAffineCoordinate();

  private StAffineCoordinate() {
    super(StGroup.INSTANCE, StExponential.INSTANCE::flattenLog);
  }
}
