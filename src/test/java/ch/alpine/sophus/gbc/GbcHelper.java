// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.Biinvariants;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate lagrainate_of(Biinvariant biinvariants, Manifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return biinvariants.lagrainate(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate harborCoordinate_of(Manifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return HarborCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate gardenCoordinate_of(Manifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return GardenCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate inversCoordinate_of( //
      Biinvariant biinvariant, Manifold vectorLogManifold) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return InverseCoordinate.of( //
            biinvariant.distances(vectorLogManifold, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriginCoordinate_of( //
      Biinvariant biinvariant, Manifold vectorLogManifold) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return KrigingCoordinate.of( //
            biinvariant.distances(vectorLogManifold, sequence), //
            vectorLogManifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(Manifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        lagrainate_of(MetricBiinvariant.EUCLIDEAN, vectorLogManifold, InversePowerVariogram.of(2)), //
        lagrainate_of(Biinvariants.LEVERAGES, vectorLogManifold, InversePowerVariogram.of(2)), //
        lagrainate_of(Biinvariants.GARDEN, vectorLogManifold, InversePowerVariogram.of(2)), //
        HsCoordinates.wrap(vectorLogManifold, MetricCoordinate.of(InversePowerVariogram.of(1))), //
        HsCoordinates.wrap(vectorLogManifold, MetricCoordinate.of(InversePowerVariogram.of(2))), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        inversCoordinate_of(MetricBiinvariant.EUCLIDEAN, vectorLogManifold), //
        inversCoordinate_of(MetricBiinvariant.EUCLIDEAN, vectorLogManifold), //
        inversCoordinate_of(Biinvariants.HARBOR, vectorLogManifold), //
        inversCoordinate_of(Biinvariants.HARBOR, vectorLogManifold), //
        kriginCoordinate_of(MetricBiinvariant.EUCLIDEAN, vectorLogManifold), //
        kriginCoordinate_of(MetricBiinvariant.EUCLIDEAN, vectorLogManifold), //
        kriginCoordinate_of(Biinvariants.HARBOR, vectorLogManifold), //
        kriginCoordinate_of(Biinvariants.HARBOR, vectorLogManifold), //
    };
  }

  public static BarycentricCoordinate[] biinvariant(Manifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        lagrainate_of(Biinvariants.LEVERAGES, vectorLogManifold, InversePowerVariogram.of(2)), //
        lagrainate_of(Biinvariants.GARDEN, vectorLogManifold, InversePowerVariogram.of(2)), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriginCoordinate_of(Biinvariants.HARBOR, vectorLogManifold), //
        kriginCoordinate_of(Biinvariants.HARBOR, vectorLogManifold), //
    };
  }

  public static BarycentricCoordinate[] biinvariant_quantity(Manifold vectorLogManifold) { //
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
