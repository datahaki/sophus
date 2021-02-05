// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.dv.GardenDistanceVector;
import ch.ethz.idsc.sophus.dv.HarborBiinvariantVector;
import ch.ethz.idsc.sophus.dv.LeveragesDistanceVector;
import ch.ethz.idsc.sophus.dv.MetricDistanceVector;
import ch.ethz.idsc.sophus.gbc.GardenCoordinate;
import ch.ethz.idsc.sophus.gbc.HarborCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.LagrangeCoordinates;
import ch.ethz.idsc.sophus.gbc.LeveragesGenesis;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020 */
public enum Biinvariants implements Biinvariant {
  /** left-invariant (biinvariant only if a biinvariant metric exists)
   * results in a symmetric distance matrix -> can use for kriging */
  METRIC {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, MetricDistanceVector.INSTANCE, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, MetricCoordinate.of(variogram), sequence);
    }

    @Override
    public String title() {
      return "Metric";
    }
  },
  AETHER { // same as metric, only for different plot option
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return METRIC.distances(vectorLogManifold, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return METRIC.coordinate(vectorLogManifold, variogram, sequence);
    }

    @Override
    public String title() {
      return "Aether";
    }
  },
  /** bi-invariant, identical to anchor */
  // TODO remove ANCHOR, rename TARGET
  TARGET {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, LeveragesDistanceVector.INSTANCE, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, LeveragesGenesis.of(variogram), sequence);
    }

    @Override
    public String title() {
      return "Leverage";
    }
  },
  /** bi-invariant
   * does not result in a symmetric distance matrix -> should not use for kriging
   * 
   * Reference:
   * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
   * by Jan Hakenberg, 2020 */
  ANCHOR {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, LeveragesDistanceVector.INSTANCE, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, LeveragesGenesis.of(variogram), sequence);
      // BarycentricCoordinate barycentricCoordinate = LeveragesCoordinate.slow(vectorLogManifold, variogram);
      // Objects.requireNonNull(sequence);
      // return point -> barycentricCoordinate.weights(sequence, point);
    }

    @Override
    public String title() {
      return "Leverage";
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  GARDEN {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return GardenDistanceVector.of(vectorLogManifold, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return GardenCoordinate.of(vectorLogManifold, variogram, sequence);
    }

    @Override
    public String title() {
      return "Garden";
    }
  }, //
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging and minimum spanning tree */
  HARBOR {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      BiinvariantVectorFunction harborDistances = HarborBiinvariantVector.of(vectorLogManifold, sequence);
      return point -> harborDistances.biinvariantVector(point).distances();
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HarborCoordinate.of(vectorLogManifold, variogram, sequence);
    }

    @Override
    public String title() {
      return "Harbor";
    }
  }, //
  ;

  @Override // from Biinvariant
  public final TensorUnaryOperator var_dist( //
      VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(vectorLogManifold, sequence);
    Objects.requireNonNull(variogram);
    return point -> tensorUnaryOperator.apply(point).map(variogram);
  }

  @Override // from Biinvariant
  public final TensorUnaryOperator weighting( //
      VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = var_dist(vectorLogManifold, variogram, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
  }

  @Override // from Biinvariant
  public final TensorUnaryOperator lagrainate( //
      VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = weighting(vectorLogManifold, variogram, sequence);
    // TODO this is inefficient, since levers are probably computed twice
    return point -> LagrangeCoordinates.of( //
        tensorUnaryOperator.apply(point), // target
        Tensor.of(sequence.stream().map(vectorLogManifold.logAt(point)::vectorLog))); // levers
  }

  /** @return */
  public abstract String title();
}
