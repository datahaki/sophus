// code by jph
package ch.ethz.idsc.sophus.krg;

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
    public TensorUnaryOperator create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      WeightingInterface weightingInterface = new AbsoluteDistances(vectorLogManifold, variogram);
      return point -> weightingInterface.weights(sequence, point);
    }
  },
  /** bi-invariant */
  RELATIVE1 {
    @Override
    public TensorUnaryOperator create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      WeightingInterface weightingInterface = new Relative1Distances(vectorLogManifold, variogram);
      return point -> weightingInterface.weights(sequence, point);
    }
  },
  /** bi-invariant */
  RELATIVE2 {
    @Override
    public TensorUnaryOperator create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new Relative2Distances(vectorLogManifold, variogram, sequence);
    }
  };

  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public abstract TensorUnaryOperator create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  public final TensorUnaryOperator affine(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    TensorUnaryOperator tensorUnaryOperator = create(vectorLogManifold, variogram, sequence);
    return point -> NormalizeTotal.FUNCTION.apply(tensorUnaryOperator.apply(point));
  }
}
