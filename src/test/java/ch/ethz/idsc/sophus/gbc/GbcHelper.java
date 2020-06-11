// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.PowerVariogram;
import ch.ethz.idsc.sophus.krg.PseudoDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate relative2Coordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return Relative2Coordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriging__Coordinate_of(PseudoDistances pseudoDistances, VectorLogManifold vectorLogManifold,
      ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return KrigingCoordinate.of( //
            pseudoDistances.weighting(vectorLogManifold, variogram, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        AbsoluteCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        AbsoluteCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        relative2Coordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        relative2Coordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE1, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE2, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE1, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE2, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] relatives(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        Relative1Coordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        relative2Coordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        relative2Coordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE1, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE2, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE1, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriging__Coordinate_of(PseudoDistances.RELATIVE2, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }
}
