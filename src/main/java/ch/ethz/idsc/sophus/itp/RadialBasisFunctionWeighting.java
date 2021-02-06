// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;

/** Careful: Radial Basis Function weights fall not in the category of generalized barycentric
 * coordinates, because Radial Basis Function Weighting does not reproduce linear functions!
 * 
 * @see Kriging */
public enum RadialBasisFunctionWeighting {
  ;
  /** @param tensorUnaryOperator
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, Tensor sequence) {
    return RadialBasisFunctionInterpolation.of(tensorUnaryOperator, sequence, IdentityMatrix.of(sequence.length()));
  }
}
