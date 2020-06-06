// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** implementation of radial basis function for homogeneous spaces
 * 
 * <p>Reference:
 * "Radial Basis Function Interpolation" in NR, 2007 */
public class RadialBasisFunctionInterpolation implements TensorUnaryOperator {
  /** @param weightingInterface to measure the length of the difference between two points
   * @param sequence of points
   * @param values
   * @return */
  public static TensorUnaryOperator normalized(TensorUnaryOperator weightingInterface, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(weightingInterface, NormalizeTotal.FUNCTION, sequence, values);
  }

  /** Careful: {@link #apply(Tensor)} returns weights that sum up to one but do not reproduce the identity!
   * 
   * @param weightingInterface
   * @param sequence of points
   * @return */
  public static TensorUnaryOperator partitions(TensorUnaryOperator weightingInterface, Tensor sequence) {
    return normalized(weightingInterface, sequence, IdentityMatrix.of(sequence.length()));
  }

  /** @param weightingInterface to measure the length of the difference between two points
   * @param sequence of points
   * @param values
   * @return */
  public static TensorUnaryOperator directized(TensorUnaryOperator weightingInterface, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(weightingInterface, t -> t, sequence, values);
  }

  /***************************************************/
  private final TensorUnaryOperator weightingInterface;
  private final TensorUnaryOperator normalize;
  private final Tensor weights;

  private RadialBasisFunctionInterpolation( //
      TensorUnaryOperator weightingInterface, TensorUnaryOperator normalize, Tensor sequence, Tensor values) {
    this.weightingInterface = weightingInterface;
    this.normalize = normalize;
    weights = LinearSolve.of( //
        Tensor.of(sequence.stream().map(point -> weightingInterface.apply(point)).map(normalize)), //
        values);
  }

  @Override
  public Tensor apply(Tensor point) {
    return normalize.apply(weightingInterface.apply(point)).dot(weights);
  }
}
