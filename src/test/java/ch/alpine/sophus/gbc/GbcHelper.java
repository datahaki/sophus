// code by jph
package ch.alpine.sophus.gbc;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.sophus.hs.GardenBiinvariant;
import ch.alpine.sophus.hs.HarborBiinvariant;
import ch.alpine.sophus.hs.HsDesign;
import ch.alpine.sophus.hs.LeveragesBiinvariant;
import ch.alpine.sophus.hs.Manifold;
import ch.alpine.sophus.hs.MetricBiinvariant;
import ch.alpine.sophus.math.var.InversePowerVariogram;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate lagrainate_of(Biinvariant biinvariants, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return biinvariants.lagrainate(variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate harborCoordinate_of(HsDesign hsDesign, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return HarborCoordinate.of(hsDesign, variogram, sequence).apply(point);
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

  public static BarycentricCoordinate inversCoordinate_of(Biinvariant biinvariant) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return InverseCoordinate.of( //
            biinvariant.distances(sequence), //
            biinvariant.hsDesign(), sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate kriginCoordinate_of(Biinvariant biinvariant) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return KrigingCoordinate.of( //
            biinvariant.distances(sequence), //
            biinvariant.hsDesign(), sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate[] barycentrics(Manifold manifold) { //
    return new BarycentricCoordinate[] { //
        lagrainate_of(new MetricBiinvariant(manifold), InversePowerVariogram.of(2)), //
        lagrainate_of(new LeveragesBiinvariant(manifold), InversePowerVariogram.of(2)), //
        lagrainate_of(new GardenBiinvariant(manifold), InversePowerVariogram.of(2)), //
        new HsCoordinates(new HsDesign(manifold), MetricCoordinate.of(InversePowerVariogram.of(1))), //
        new HsCoordinates(new HsDesign(manifold), MetricCoordinate.of(InversePowerVariogram.of(2))), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(2)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(new HsDesign(manifold), InversePowerVariogram.of(1)), //
        harborCoordinate_of(new HsDesign(manifold), InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(new HsDesign(manifold), InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(new HsDesign(manifold), InversePowerVariogram.of(2)), //
        inversCoordinate_of(new MetricBiinvariant(manifold)), //
        inversCoordinate_of(new HarborBiinvariant(manifold)), //
        kriginCoordinate_of(new MetricBiinvariant(manifold)), //
        kriginCoordinate_of(new HarborBiinvariant(manifold)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant(Manifold manifold) { //
    return new BarycentricCoordinate[] { //
        lagrainate_of(new LeveragesBiinvariant(manifold), InversePowerVariogram.of(2)), //
        lagrainate_of(new GardenBiinvariant(manifold), InversePowerVariogram.of(2)), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(1)), //
        gardenCoordinate_of(manifold, InversePowerVariogram.of(2)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(1)), //
        // LeveragesCoordinate.slow(vectorLogManifold, InversePowerVariogram.of(2)), //
        harborCoordinate_of(new HsDesign(manifold), InversePowerVariogram.of(1)), //
        harborCoordinate_of(new HsDesign(manifold), InversePowerVariogram.of(2)), //
        LeveragesCoordinate.of(new HsDesign(manifold), InversePowerVariogram.of(1)), //
        LeveragesCoordinate.of(new HsDesign(manifold), InversePowerVariogram.of(2)), //
        kriginCoordinate_of(new HarborBiinvariant(manifold)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant_quantity(Manifold manifold) { //
    return new BarycentricCoordinate[] { //
        harborCoordinate_of(new HsDesign(manifold), InversePowerVariogram.of(2)) //
    };
  }
}
