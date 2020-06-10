// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.gbc.AbsoluteCoordinate;
import ch.ethz.idsc.sophus.gbc.BarycentricCoordinate;
import ch.ethz.idsc.sophus.gbc.Relative1Coordinate;
import ch.ethz.idsc.sophus.gbc.Relative2Coordinate;
import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum PseudoDistances {
  /** left-invariant */
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
  /** bi-invariant */
  RELATIVE1 {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      Relative1Distances relative1Distances = new Relative1Distances(vectorLogManifold, variogram);
      return point -> relative1Distances.biinvariantVector(sequence, point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      BarycentricCoordinate barycentricCoordinate = Relative1Coordinate.of(vectorLogManifold, variogram);
      return point -> barycentricCoordinate.weights(sequence, point);
    }
  },
  /** bi-invariant */
  RELATIVE2 {
    @Override
    public TensorUnaryOperator weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      Relative2Distances relative2Distances = new Relative2Distances(vectorLogManifold, variogram, sequence);
      return point -> relative2Distances.biinvariantVector(point).vector();
    }

    @Override
    public TensorUnaryOperator coordinate(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return Relative2Coordinate.of(vectorLogManifold, variogram, sequence);
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
