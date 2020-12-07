// code by jph
package ch.ethz.idsc.sophus.hs;

import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GardenCoordinate;
import ch.ethz.idsc.sophus.gbc.HarborCoordinate;
import ch.ethz.idsc.sophus.gbc.HsCoordinates;
import ch.ethz.idsc.sophus.gbc.LagrangeCoordinate;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.gbc.TargetCoordinate;
import ch.ethz.idsc.sophus.krg.GardenDistances;
import ch.ethz.idsc.sophus.krg.HarborDistances;
import ch.ethz.idsc.sophus.krg.LeverageDistances;
import ch.ethz.idsc.sophus.krg.MetricDistances;
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
      return HsCoordinates.wrap(vectorLogManifold, MetricDistances.INSTANCE, sequence);
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
  TARGET {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, LeverageDistances.INSTANCE, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HsCoordinates.wrap(vectorLogManifold, TargetCoordinate.of(variogram), sequence);
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
      return HsCoordinates.wrap(vectorLogManifold, LeverageDistances.INSTANCE, sequence);
    }

    @Override // from Biinvariant
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = LeverageCoordinate.slow(vectorLogManifold, variogram);
      Objects.requireNonNull(sequence);
      return point -> barycentricCoordinate.weights(sequence, point);
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
      return GardenDistances.of(vectorLogManifold, sequence);
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
   * results in a symmetric distance matrix -> can use for kriging */
  HARBOR {
    @Override // from Biinvariant
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      HarborDistances harborDistances = HarborDistances.frobenius(vectorLogManifold, sequence);
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
  public final TensorUnaryOperator var_dist(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(vectorLogManifold, sequence);
    Objects.requireNonNull(variogram);
    return point -> tensorUnaryOperator.apply(point).map(variogram);
  }

  @Override // from Biinvariant
  public final TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(vectorLogManifold, sequence);
    Objects.requireNonNull(variogram);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point).map(variogram));
  }

  @Override // from Biinvariant
  public final TensorUnaryOperator lagrainate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator weighting = weighting(vectorLogManifold, variogram, sequence);
    return point -> {
      Tensor target = weighting.apply(point);
      TangentSpace tangentSpace = vectorLogManifold.logAt(point);
      Tensor levers = Tensor.of(sequence.stream().map(tangentSpace::vectorLog));
      return LagrangeCoordinate.fit(target, levers);
    };
  }

  /** @return */
  public abstract String title();
}
