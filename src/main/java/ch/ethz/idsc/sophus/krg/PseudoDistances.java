// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.PairwiseCoordinate;
import ch.ethz.idsc.sophus.gbc.SolitaryCoordinate;
import ch.ethz.idsc.sophus.gbc.SolitaryMahalanobisCoordinate;
import ch.ethz.idsc.sophus.gbc.StarlikeCoordinate;
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
  SOLITARY {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      SolitaryDistances diagonalDistances = new SolitaryDistances(vectorLogManifold, variogram);
      return point -> diagonalDistances.biinvariantVector(sequence, point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = SolitaryCoordinate.of(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }
  },
  /** bi-invariant */
  MONOMAHA {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      TensorUnaryOperator tensorUnaryOperator = SolitaryMahalanobisDistances.of(vectorLogManifold, variogram, sequence);
      return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return SolitaryMahalanobisCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  },
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  PAIRWISE {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      PairwiseDistances completeDistances = PairwiseDistances.frobenius(vectorLogManifold, variogram, sequence);
      return point -> completeDistances.biinvariantVector(point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return PairwiseCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  }, //
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  STARLIKE {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      TensorUnaryOperator tensorUnaryOperator = StarlikeDistances.of(vectorLogManifold, variogram, sequence);
      return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return StarlikeCoordinate.of(vectorLogManifold, variogram, sequence);
    }
  }, //
  /** bi-invariant
   * results in a symmetric distance matrix -> can use for kriging */
  NORM2 {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      PairwiseDistances completeDistances = PairwiseDistances.norm2(vectorLogManifold, variogram, sequence);
      return point -> completeDistances.biinvariantVector(point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      PairwiseDistances pairwiseDistances = PairwiseDistances.norm2(vectorLogManifold, variogram, sequence);
      return point -> pairwiseDistances.biinvariantVector(point).coordinate();
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
