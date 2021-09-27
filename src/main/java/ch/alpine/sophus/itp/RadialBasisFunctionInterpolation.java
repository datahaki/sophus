// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.hs.Biinvariant;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.LeastSquares;

/** implementation of radial basis function for homogeneous spaces
 * 
 * Quote: "The weights are determined by requiring that the interpolation be exact at all
 * the known data points. That is equivalent to solving a set of N linear equations in N
 * unknowns for the w’s."
 * 
 * Careful: Radial Basis Function weights fall not in the category of generalized barycentric
 * coordinates, because Radial Basis Function Weighting does not reproduce linear functions!
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

  /** @param tensorUnaryOperator
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(TensorUnaryOperator tensorUnaryOperator, Tensor sequence) {
    return of(tensorUnaryOperator, sequence, IdentityMatrix.of(sequence.length()));
  }

  // ---
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
