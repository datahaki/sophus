// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Biinvariant;
import ch.ethz.idsc.sophus.krg.GardenDistances;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.PowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate springCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        BarycentricCoordinate barycentricCoordinate = //
            GardenCoordinate.of(vectorLogManifold, GardenDistances.of(vectorLogManifold, variogram, sequence));
        return barycentricCoordinate.weights(sequence, point);
      }
    };
  }

  public static BarycentricCoordinate harborCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return HarborCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate targetCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return TargetCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriginCoordinate_of(Biinvariant biinvariant, VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return InverseCoordinate.of( //
            biinvariant.distances(vectorLogManifold, variogram, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        MetricCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        MetricCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        springCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        springCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        targetCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        targetCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriginCoordinate_of(Biinvariant.METRIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.METRIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        springCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        springCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        targetCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        targetCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant_quantity(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }
}
