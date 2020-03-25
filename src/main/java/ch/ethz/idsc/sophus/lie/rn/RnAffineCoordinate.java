// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.LieFlattenLogManifold;
import ch.ethz.idsc.sophus.math.win.AffineCoordinate;

/** Hint: class exists only for verification purpose.
 * Functionality is identical to {@link AffineCoordinate} */
public enum RnAffineCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = //
      HsBarycentricCoordinate.affine(LieFlattenLogManifold.of(RnGroup.INSTANCE, RnExponential.INSTANCE::log));
}
