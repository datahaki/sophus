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
  public static BarycentricCoordinate completeCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return CompleteCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriging__Coordinate_of(PseudoDistances pseudoDistances, VectorLogManifold vectorLogManifold,
      ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return InverseCoordinate.of( //
            pseudoDistances.weighting(vectorLogManifold, variogram, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        AbsoluteCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        AbsoluteCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        DiagonalCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        DiagonalCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        completeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        completeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.ABSOLUTE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.ABSOLUTE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriging__Coordinate_of(PseudoDistances.COMPLETE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.COMPLETE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] relatives(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        DiagonalCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        DiagonalCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        completeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        completeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.COMPLETE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.COMPLETE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }
}
