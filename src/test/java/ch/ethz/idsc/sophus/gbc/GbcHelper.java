// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate[] barycentrics(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        AbsoluteCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        AbsoluteCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        Relative0Coordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        Relative0Coordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
    };
  }

  public static BarycentricCoordinate[] relatives(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(2)) };
  }
}
