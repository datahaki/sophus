// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import ch.ethz.idsc.sophus.hs.HsBarycentricCoordinate;
import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.lie.rn.RnNormSquared;
import ch.ethz.idsc.sophus.math.win.BarycentricCoordinate;
import ch.ethz.idsc.sophus.math.win.InverseNorm;

public enum SpdBarycentricCoordinate {
  ;
  public static final BarycentricCoordinate INSTANCE = //
      new HsBarycentricCoordinate(SpdManifold.INSTANCE, InverseNorm.of(RnNorm.INSTANCE));
  public static final BarycentricCoordinate SQUARED = //
      new HsBarycentricCoordinate(SpdManifold.INSTANCE, InverseNorm.of(RnNormSquared.INSTANCE));
}
