// code by jph
package ch.ethz.idsc.sophus.lie.se2c;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.LieAffineCoordinate;

public class Se2CoveringAffineCoordinate extends LieAffineCoordinate {
  public static final BarycentricCoordinate INSTANCE = new Se2CoveringAffineCoordinate();

  private Se2CoveringAffineCoordinate() {
    super(Se2CoveringGroup.INSTANCE, Se2CoveringExponential.INSTANCE::log);
  }
}
