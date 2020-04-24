// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Radial Basis Function Interpolation" in NR, 2007 */
public enum RadialBasisFunctionInterpolation {
  ;
  /** @param tensorNorm to measure the length of the difference between two points
   * @param sequence
   * @param values
   * @return */
  public static TensorUnaryOperator normalized(PseudoDistances pseudoDistances, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionImpl(pseudoDistances, NormalizeTotal.FUNCTION, sequence, values);
  }

  /** Careful: {@link #apply(Tensor)} returns weights that sum up to one but do not reproduce the identity!
   * 
   * @param tensorNorm
   * @param sequence of points in R^n
   * @return */
  public static TensorUnaryOperator barycentric(PseudoDistances pseudoDistances, Tensor sequence) {
    return normalized(pseudoDistances, sequence, IdentityMatrix.of(sequence.length()));
  }
}
