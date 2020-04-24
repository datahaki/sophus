// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum PseudoDistances {
  /** left-invariant */
  ABSOLUTE {
    @Override
    public WeightingInterface of(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
      return new AbsoluteDistances(flattenLogManifold, variogram);
    }
  },
  /** bi-invariant */
  RELATIVE {
    @Override
    public WeightingInterface of(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
      return new RelativeDistances(flattenLogManifold, variogram);
    }
  };

  /** @param flattenLogManifold
   * @param variogram
   * @return */
  public abstract WeightingInterface of(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram);

  /***************************************************/
  /** Careful: Every evaluation of returned WeightingInterface is expensive!
   * If multiple evaluations are required for the same sequence, then use
   * {@link #barycentric(FlattenLogManifold, ScalarUnaryOperator, Tensor)}
   * 
   * @param flattenLogManifold
   * @param variogram
   * @return */
  public WeightingInterface weighting(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
    return new WeightingImpl(Objects.requireNonNull(flattenLogManifold), Objects.requireNonNull(variogram));
  }

  /***************************************************/
  // class non-static since invokes #barycentric
  private class WeightingImpl implements WeightingInterface, Serializable {
    private final FlattenLogManifold flattenLogManifold;
    private final ScalarUnaryOperator variogram;

    public WeightingImpl(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
      this.flattenLogManifold = flattenLogManifold;
      this.variogram = variogram;
    }

    @Override // from WeightingInterface
    public Tensor weights(Tensor sequence, Tensor point) {
      // TODO is this the same as radial basis functions!?
      return Krigings.barycentric(of(flattenLogManifold, variogram), sequence).estimate(point);
    }
  }
}
