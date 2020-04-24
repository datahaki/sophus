package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

public enum RadialBasisFunctions {
  /** left-invariant */
  ABSOLUTE {
    @Override
    PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new AbsoluteDistances(flattenLogManifold, variogram, sequence);
    }
  },
  /** bi-invariant */
  RELATIVE {
    @Override
    PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new RelativeDistances(flattenLogManifold, variogram, sequence);
    }
  };

  /* package */ abstract PseudoDistances pseudoDistances( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence);
}
