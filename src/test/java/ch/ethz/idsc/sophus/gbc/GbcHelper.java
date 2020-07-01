// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.Biinvariant;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.PowerVariogram;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate harborCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return HarborCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate gardenCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return GardenCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate inversCoordinate_of(Biinvariant biinvariant, VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return InverseCoordinate.of( //
            biinvariant.distances(vectorLogManifold, variogram, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriginCoordinate_of(Biinvariant biinvariant, VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return KrigingCoordinate.of( //
            biinvariant.distances(vectorLogManifold, variogram, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        InverseDistanceCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        InverseDistanceCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        InverseLeverageCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        InverseLeverageCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        InverseLeverageCoordinate.fast(vectorLogManifold, InversePowerVariogram.of(1)), //
        InverseLeverageCoordinate.fast(vectorLogManifold, InversePowerVariogram.of(2)), //
        inversCoordinate_of(Biinvariant.METRIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        inversCoordinate_of(Biinvariant.METRIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        inversCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        inversCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriginCoordinate_of(Biinvariant.METRIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.METRIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        InverseLeverageCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        InverseLeverageCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        InverseLeverageCoordinate.fast(vectorLogManifold, InversePowerVariogram.of(1)), //
        InverseLeverageCoordinate.fast(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant_quantity(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        // AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        // AnchorCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        // harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        // harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        // kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriginCoordinate_of(Biinvariant.HARBOR, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }
}
