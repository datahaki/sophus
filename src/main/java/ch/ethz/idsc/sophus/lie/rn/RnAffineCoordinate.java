// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.math.win.AffineCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.LieAffineCoordinate;

/** Hint: class exists only for verification purpose.
 * Functionality is identical to {@link AffineCoordinate} */
public class RnAffineCoordinate extends LieAffineCoordinate {
  public static final BarycentricCoordinate INSTANCE = new RnAffineCoordinate();

  private RnAffineCoordinate() {
    super(RnGroup.INSTANCE, RnExponential.INSTANCE::log);
  }
}
