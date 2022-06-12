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
  public static BarycentricCoordinate lagrainate_of(Biinvariant biinvariants, Manifold manifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return biinvariants.lagrainate(manifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate harborCoordinate_of(Manifold manifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return HarborCoordinate.of(manifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate gardenCoordinate_of(Manifold manifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return GardenCoordinate.of(manifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate inversCoordinate_of( //
      Biinvariant biinvariant, Manifold manifold) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return InverseCoordinate.of( //
            biinvariant.distances(manifold, sequence), //
            manifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriginCoordinate_of( //
      Biinvariant biinvariant, Manifold manifold) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return KrigingCoordinate.of( //
            biinvariant.distances(manifold, sequence), //
            manifold, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(Manifold manifold) { //
    return new BarycentricCoordinate[] { //
        lagrainate_of(MetricBiinvariant.EUCLIDEAN, manifold, InversePowerVariogram.of(2)), //
        lagrainate_of(Biinvariants.LEVERAGES, manifold, InversePowerVariogram.of(2)), //
        lagrainate_of(Biinvariants.GARDEN, manifold, InversePowerVariogram.of(2)), //
        HsCoordinates.of(manifold, MetricCoordinate.of(InversePowerVariogram.of(1))), //
        HsCoordinates.of(manifold, MetricCoordinate.of(InversePowerVariogram.of(2))), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(2)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(manifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(manifold, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(manifold, InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(manifold, InversePowerVariogram.of(2)), //
        inversCoordinate_of(MetricBiinvariant.EUCLIDEAN, manifold), //
        inversCoordinate_of(MetricBiinvariant.EUCLIDEAN, manifold), //
        inversCoordinate_of(Biinvariants.HARBOR, manifold), //
        inversCoordinate_of(Biinvariants.HARBOR, manifold), //
        kriginCoordinate_of(MetricBiinvariant.EUCLIDEAN, manifold), //
        kriginCoordinate_of(MetricBiinvariant.EUCLIDEAN, manifold), //
        kriginCoordinate_of(Biinvariants.HARBOR, manifold), //
        kriginCoordinate_of(Biinvariants.HARBOR, manifold), //
    };
  }

  public static BarycentricCoordinate[] biinvariant(Manifold manifold) { //
    return new BarycentricCoordinate[] { //
        lagrainate_of(Biinvariants.LEVERAGES, manifold, InversePowerVariogram.of(2)), //
        lagrainate_of(Biinvariants.GARDEN, manifold, InversePowerVariogram.of(2)), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(2)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(manifold, InversePowerVariogram.of(1)), //
        harborCoordinate_of(manifold, InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(manifold, InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(manifold, InversePowerVariogram.of(2)), //
        kriginCoordinate_of(Biinvariants.HARBOR, manifold), //
        kriginCoordinate_of(Biinvariants.HARBOR, manifold), //
    };
  }

  public static BarycentricCoordinate[] biinvariant_quantity(Manifold manifold) { //
    return new BarycentricCoordinate[] { //
        harborCoordinate_of(manifold, InversePowerVariogram.of(2)) //
    };
  }
}
