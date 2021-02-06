// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.hs.Biinvariant;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;
import ch.ethz.idsc.tensor.mat.LeastSquares;

/** implementation of radial basis function for homogeneous spaces
 * 
 * Quote: "The weights are determined by requiring that the interpolation be exact at all
 * the known data points. That is equivalent to solving a set of N linear equations in N
 * unknowns for the w’s."
 * 
 * <p>Reference:
 * "Radial Basis Function Interpolation", Section 3.7.1 in NR, 2007
 * 
 * @see Biinvariant
 * @see Kriging */
public class RadialBasisFunctionInterpolation implements TensorUnaryOperator {
  /** @param tensorUnaryOperator to compute distance vector of a given point to the points in sequence
   * @param sequence of points
   * @param values
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(tensorUnaryOperator, sequence, values);
  }

  /***************************************************/
  private final TensorUnaryOperator tensorUnaryOperator;
  private final Tensor weights;

  private RadialBasisFunctionInterpolation( //
      TensorUnaryOperator tensorUnaryOperator, Tensor sequence, Tensor values) {
    this.tensorUnaryOperator = tensorUnaryOperator;
    weights = LeastSquares.of( //
        Tensor.of(sequence.stream().map(tensorUnaryOperator)), // distance matrix as in Kriging
        values);
  }

  @Override
  public Tensor apply(Tensor point) {
    return tensorUnaryOperator.apply(point).dot(weights);
  }
}
