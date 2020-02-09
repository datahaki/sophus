// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.math.win.InverseNorm;
import ch.ethz.idsc.sophus.math.win.LieInverseDistanceCoordinates;

public enum ScInverseDistanceCoordinates {
  ;
  public static final LieInverseDistanceCoordinates INSTANCE = new LieInverseDistanceCoordinates( //
      ScGroup.INSTANCE, ScExponential.INSTANCE::log, InverseNorm.of(ScVectorNorm.INSTANCE));
}
