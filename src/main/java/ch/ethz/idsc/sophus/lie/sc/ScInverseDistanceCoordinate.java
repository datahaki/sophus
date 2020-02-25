// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.lie.LieBarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

public enum ScInverseDistanceCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = new LieBarycentricCoordinate( //
      ScGroup.INSTANCE, //
      ScExponential.INSTANCE::log, //
      InverseNorm.of(ScVectorNorm.INSTANCE));
}
