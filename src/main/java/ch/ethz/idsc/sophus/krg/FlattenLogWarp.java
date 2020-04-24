// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum FlattenLogWarp {
  /** left-invariant */
  ABSOLUTE {
    @Override
    public PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new AbsoluteDistances(flattenLogManifold, variogram, sequence);
    }
  },
  /** bi-invariant */
  RELATIVE {
    @Override
    public PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new RelativeDistances(flattenLogManifold, variogram, sequence);
    }
  };

  /** @param flattenLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public abstract PseudoDistances pseudoDistances( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

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
      return Krigings.barycentric(pseudoDistances(flattenLogManifold, variogram, sequence), sequence).estimate(point);
    }
  }
}
