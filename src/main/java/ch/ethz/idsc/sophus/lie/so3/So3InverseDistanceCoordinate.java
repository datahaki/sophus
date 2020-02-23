// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieBarycentricCoordinate;

/** barycentric coordinates that are invariant under left-action, right-action and inversion */
public enum So3InverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieBarycentricCoordinate( //
      So3Group.INSTANCE, //
      So3Exponential.INSTANCE::log, //
      InverseNorm.of(RnNorm.INSTANCE));
}
