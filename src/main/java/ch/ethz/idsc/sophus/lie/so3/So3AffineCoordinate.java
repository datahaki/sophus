// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.LieAffineCoordinate;

public class So3AffineCoordinate extends LieAffineCoordinate {
  public static final BarycentricCoordinate INSTANCE = new So3AffineCoordinate();

  private So3AffineCoordinate() {
    super(So3Group.INSTANCE, So3Exponential.INSTANCE::log);
  }
}