// code by jph
package ch.ethz.idsc.sophus.gbc;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.krg.InversePowerVariogram;
import ch.ethz.idsc.sophus.krg.PowerVariogram;
import ch.ethz.idsc.sophus.krg.PseudoDistances;
import ch.ethz.idsc.sophus.krg.StarlikeDistances;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum GbcHelper {
  ;
  public static BarycentricCoordinate starlikeCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        TensorUnaryOperator tensorUnaryOperator = StarlikeDistances.of(vectorLogManifold, variogram, sequence);
        return ProjectedDistancesCoordinate.of(vectorLogManifold, tensorUnaryOperator, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate pairwiseCoordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return PairwiseCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
      }
    };
  }

  public static BarycentricCoordinate mahalan1Coordinate_of(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram) {
    return new BarycentricCoordinate() {
      @Override
      public Tensor weights(Tensor sequence, Tensor point) {
        return SolitaryMahalanobisCoordinate.of(vectorLogManifold, variogram, sequence).apply(point);
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
        starlikeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        starlikeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        SolitaryCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        SolitaryCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        pairwiseCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        pairwiseCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        mahalan1Coordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        mahalan1Coordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.ABSOLUTE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.ABSOLUTE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        kriging__Coordinate_of(PseudoDistances.PAIRWISE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.PAIRWISE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        starlikeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        starlikeCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        SolitaryCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        SolitaryCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        pairwiseCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        pairwiseCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        mahalan1Coordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        mahalan1Coordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.PAIRWISE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.PAIRWISE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }

  public static BarycentricCoordinate[] biinvariant_quantity(VectorLogManifold vectorLogManifold) { //
    return new BarycentricCoordinate[] { //
        SolitaryCoordinate.of(vectorLogManifold, InversePowerVariogram.of(1)), //
        SolitaryCoordinate.of(vectorLogManifold, InversePowerVariogram.of(2)), //
        pairwiseCoordinate_of(vectorLogManifold, InversePowerVariogram.of(1)), //
        pairwiseCoordinate_of(vectorLogManifold, InversePowerVariogram.of(2)), //
        kriging__Coordinate_of(PseudoDistances.PAIRWISE, vectorLogManifold, PowerVariogram.of(1, 1)), //
        kriging__Coordinate_of(PseudoDistances.PAIRWISE, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1)), //
        // kriging__Coordinate_of(PseudoDistances.GEODESIC, vectorLogManifold, PowerVariogram.of(1, 1.5)), //
    };
  }
}
