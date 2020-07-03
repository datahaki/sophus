// code by jph
package ch.ethz.idsc.sophus.krg;

import java.util.Objects;

import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.GardenCoordinate;
import ch.ethz.idsc.sophus.gbc.HarborCoordinate;
import ch.ethz.idsc.sophus.gbc.LeverageCoordinate;
import ch.ethz.idsc.sophus.gbc.MetricCoordinate;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum Biinvariant {
  /** left-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  METRIC {
    @Override
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      WeightingInterface weightingInterface = new MetricDistances(vectorLogManifold);
      return point -> weightingInterface.weights(sequence, point);
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = MetricCoordinate.of(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }

    @Override
    public String title() {
      return "Inverse Distance";
    }
  },
  /** bi-invariant, identical to anchor */
  TARGET {
    @Override
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      WeightingInterface weightingInterface = LeverageDistances.of(vectorLogManifold);
      return point -> weightingInterface.weights(sequence, point);
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = LeverageCoordinate.of(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }

    @Override
    public String title() {
      return "Inverse Leverage";
    }
  },
  /** bi-invariant
   * does not result in a symmetric distance matrix -> should not use for kriging
   * 
   * Reference:
   * "Biinvariant Generalized Barycentric Coordinates on Lie Groups"
   * by Jan Hakenberg, 2020 */
  ANCHOR {
    @Override
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      WeightingInterface weightingInterface = LeverageDistances.of(vectorLogManifold);
      return point -> weightingInterface.weights(sequence, point);
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = LeverageCoordinate.slow(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }

    @Override
    public String title() {
      return "Inverse Leverage";
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  GARDEN {
    @Override
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      return GardenDistances.of(vectorLogManifold, sequence);
    }

    @Override
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
    @Override
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      HarborDistances harborDistances = HarborDistances.frobenius(vectorLogManifold, sequence);
      return point -> harborDistances.biinvariantVector(point).distances();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return HarborCoordinate.of(vectorLogManifold, variogram, sequence);
    }

    @Override
    public String title() {
      return "Harbor";
    }
  }, //
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  NORM2 {
    @Override
    public TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence) {
      HarborDistances harborDistances = HarborDistances.norm2(vectorLogManifold, sequence);
      return point -> harborDistances.biinvariantVector(point).distances();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      HarborDistances harborDistances = HarborDistances.norm2(vectorLogManifold, sequence);
      Objects.requireNonNull(variogram);
      return point -> harborDistances.biinvariantVector(point).coordinate(variogram);
    }

    @Override
    public String title() {
      return "Norm2";
    }
  }, //
  ;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator of weights that generally do not sum up to one */
  public abstract TensorUnaryOperator distances(VectorLogManifold vectorLogManifold, Tensor sequence);

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator that provides affine weights */
  public final TensorUnaryOperator var_dist(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(vectorLogManifold, sequence);
    return point -> tensorUnaryOperator.apply(point).map(variogram);
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator that provides affine weights */
  public final TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = distances(vectorLogManifold, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point).map(variogram));
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator that provides barycentric coordinates */
  public abstract TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /** @return */
  public abstract String title();
}
