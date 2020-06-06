// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.VectorLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum PseudoDistances {
  /** left-invariant */
  ABSOLUTE {
    @Override
    public WeightingInterface create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new AbsoluteDistances(vectorLogManifold, variogram);
    }
  },
  /** bi-invariant */
  RELATIVE1 {
    @Override
    public WeightingInterface create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new Relative1Distances(vectorLogManifold, variogram);
    }
  };

  /** @param vectorLogManifold
   * @param variogram
   * @return */
  public abstract WeightingInterface create(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /***************************************************/
  /** Careful: Every evaluation of returned WeightingInterface is expensive!
   * If multiple evaluations are required for the same sequence, then use
   * {@link #barycentric(VectorLogManifold, ScalarUnaryOperator, Tensor)}
   * 
   * @param vectorLogManifold
   * @param variogram
   * @return */
  public WeightingInterface weighting(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return new WeightingImpl(Objects.requireNonNull(vectorLogManifold), Objects.requireNonNull(variogram), sequence);
  }

  /***************************************************/
  // class non-static since invokes #barycentric
  private class WeightingImpl implements WeightingInterface, Serializable {
    private final WeightingInterface weightingInterface;

    public WeightingImpl(VectorLogManifold vectorLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      weightingInterface = create(vectorLogManifold, variogram, sequence);
    }

    @Override // from WeightingInterface
    public Tensor weights(Tensor sequence, Tensor point) {
      // TODO is this the same as radial basis functions!?
      return Kriging.barycentric(weightingInterface, sequence).estimate(point);
    }
  }
}
