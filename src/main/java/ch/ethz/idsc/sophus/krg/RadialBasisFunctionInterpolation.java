// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.math.NormalizeTotal;
import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

/** Reference:
 * "Radial Basis Function Interpolation" in NR, 2007 */
public class RadialBasisFunctionInterpolation implements TensorUnaryOperator {
  /** @param weightingInterface to measure the length of the difference between two points
   * @param sequence
   * @param values
   * @return */
  public static TensorUnaryOperator of(WeightingInterface weightingInterface, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(weightingInterface, t -> t, sequence, values);
  }

  /** @param weightingInterface to measure the length of the difference between two points
   * @param sequence
   * @param values
   * @return */
  public static TensorUnaryOperator normalized(WeightingInterface weightingInterface, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(weightingInterface, NormalizeTotal.FUNCTION, sequence, values);
  }

  /** Careful: {@link #apply(Tensor)} returns weights that sum up to one but do not reproduce the identity!
   * 
   * @param weightingInterface
   * @param sequence of points in R^n
   * @return */
  public static TensorUnaryOperator partitions(WeightingInterface weightingInterface, Tensor sequence) {
    return normalized(weightingInterface, sequence, IdentityMatrix.of(sequence.length()));
  }

  /***************************************************/
  private final WeightingInterface weightingInterface;
  private final TensorUnaryOperator normalize;
  private final Tensor sequence;
  private final Tensor weights;

  private RadialBasisFunctionInterpolation( //
      WeightingInterface weightingInterface, TensorUnaryOperator normalize, Tensor sequence, Tensor values) {
    this.weightingInterface = weightingInterface;
    this.normalize = normalize;
    this.sequence = sequence;
    weights = LinearSolve.of( //
        Tensor.of(sequence.stream().map(point -> weightingInterface.weights(sequence, point)).map(normalize)), //
        values);
  }

  @Override
  public Tensor apply(Tensor point) {
    return normalize.apply(weightingInterface.weights(sequence, point)).dot(weights);
  }
}
