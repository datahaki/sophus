// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Radial Basis Function Interpolation" in NR, 2007 */
public class RadialBasisFunctionInterpolation implements TensorUnaryOperator {
  /** @param tensorNorm to measure the length of the difference between two points
   * @param sequence
   * @param values
   * @return */
  public static TensorUnaryOperator of(TensorNorm tensorNorm, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(v -> v, tensorNorm, sequence, values);
  }

  /** @param tensorNorm to measure the length of the difference between two points
   * @param sequence
   * @param values
   * @return */
  public static TensorUnaryOperator normalized(TensorNorm tensorNorm, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(NormalizeTotal.FUNCTION, tensorNorm, sequence, values);
  }

  /** Careful: {@link #apply(Tensor)} returns weights that sum up to one but do not reproduce the identity!
   * 
   * @param tensorNorm
   * @param sequence of points in R^n
   * @return */
  public static TensorUnaryOperator barycentric(TensorNorm tensorNorm, Tensor sequence) {
    return normalized(tensorNorm, sequence, IdentityMatrix.of(sequence.length()));
  }

  /***************************************************/
  private final TensorUnaryOperator normalize;
  private final TensorNorm tensorNorm;
  private final Tensor sequence;
  private final Tensor weights;

  private RadialBasisFunctionInterpolation(TensorUnaryOperator normalize, TensorNorm tensorNorm, Tensor sequence, Tensor values) {
    this.normalize = normalize;
    this.tensorNorm = tensorNorm;
    this.sequence = sequence;
    weights = LinearSolve.of(Tensor.of(sequence.stream().map(this::distances)), values);
  }

  @Override
  public Tensor apply(Tensor x) {
    return distances(x).dot(weights);
  }

  private Tensor distances(Tensor x) {
    return normalize.apply(Tensor.of(sequence.stream() //
        .map(x::subtract) //
        .map(tensorNorm::norm)));
  }
}
