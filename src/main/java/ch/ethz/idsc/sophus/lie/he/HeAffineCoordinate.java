// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.LieAffineCoordinate;

public class HeAffineCoordinate extends LieAffineCoordinate {
  public static final BarycentricCoordinate INSTANCE = new HeAffineCoordinate();

  private HeAffineCoordinate() {
    super(HeGroup.INSTANCE, HeExponential::flattenLog);
  }
}
