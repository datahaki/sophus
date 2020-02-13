// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.InverseDistanceCoordinates;
import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinates;

public enum So3InverseDistanceCoordinates {
  ;
  public static final InverseDistanceCoordinates INSTANCE = new LieInverseDistanceCoordinates( //
      So3Group.INSTANCE, So3Exponential.INSTANCE::log, InverseNorm.of(RnNorm.INSTANCE));
}
