// code by jph
package ch.ethz.idsc.sophus.lie.he;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.hs.ProjectedCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.tensor.RealScalar;

/** HeInverseDistanceCoordinate is invariant under left-action */
public enum HeInverseDistanceCoordinate {
  ;
  public static final ProjectedCoordinate INSTANCE = HsBarycentricCoordinate.custom( //
      HeManifold.INSTANCE, InverseNorm.of(new HeTarget(RnNorm.INSTANCE, RealScalar.ONE)));
}
