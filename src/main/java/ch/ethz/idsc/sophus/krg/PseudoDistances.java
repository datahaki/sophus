// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.CompleteCoordinate;
import ch.ethz.idsc.sophus.gbc.DiagonalCoordinate;
import ch.ethz.idsc.sophus.gbc.Mahalanobis2Coordinate;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum PseudoDistances {
  /** left-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  ABSOLUTE {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      WeightingInterface weightingInterface = new AbsoluteDistances(vectorLogManifold, variogram);
      return point -> weightingInterface.weights(sequence, point);
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = AbsoluteCoordinate.of(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }
  },
  /** bi-invariant
   * does not result in a symmetric distance matrix -> should not use for kriging */
  DIAGONAL {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      DiagonalDistances diagonalDistances = new DiagonalDistances(vectorLogManifold, variogram);
      return point -> diagonalDistances.biinvariantVector(sequence, point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = DiagonalCoordinate.of(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  COMPLETE {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      CompleteDistances completeDistances = CompleteDistances.frobenius(vectorLogManifold, variogram, sequence);
      return point -> completeDistances.biinvariantVector(point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return CompleteCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  }, //
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  NORM2 {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      CompleteDistances completeDistances = CompleteDistances.norm2(vectorLogManifold, variogram, sequence);
      return point -> completeDistances.biinvariantVector(point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      CompleteDistances completeDistances = CompleteDistances.norm2(vectorLogManifold, variogram, sequence);
      return point -> completeDistances.biinvariantVector(point).coordinate();
    }
  }, //
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  MAHALAN2 {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      TensorUnaryOperator completeDistances = Mahalanobis2Distances.of(vectorLogManifold, variogram, sequence);
      return point -> NormalizeTotal.FUNCTION.apply(completeDistances.apply(point));
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return Mahalanobis2Coordinate.of(vectorLogManifold, variogram, sequence);
    }
  }, //
  ;

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator of weights that do not necessarily sum up to one */
  public abstract TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return operator that provides affine weights */
  public final TensorUnaryOperator normalized(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = weighting(vectorLogManifold, variogram, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
  }

  /** @param vectorLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public abstract TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);
}
