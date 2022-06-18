// code by jph
package ch.alpine.sophus.itp;

import ch.alpine.sophus.dv.Biinvariant;
import ch.alpine.sophus.hs.Sedarim;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.pi.LeastSquares;

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
  /** @param sedarim to compute distance vector of a given point to the points in sequence
   * @param sequence of points
   * @param values
   * @return */
  public static TensorUnaryOperator of(Sedarim sedarim, Tensor sequence, Tensor values) {
    return new RadialBasisFunctionInterpolation(sedarim, sequence, values);
  }

  /** @param sedarim
   * @param sequence
   * @return */
  public static TensorUnaryOperator of(Sedarim sedarim, Tensor sequence) {
    // TODO SOPHUS IMPL can be improved since no need dotting with id-matrix
    return of(sedarim, sequence, IdentityMatrix.of(sequence.length()));
  }

  // ---
  private final Sedarim sedarim;
  private final Tensor weights;

  private RadialBasisFunctionInterpolation(Sedarim sedarim, Tensor sequence, Tensor values) {
    this.sedarim = sedarim;
    weights = LeastSquares.of( //
        Tensor.of(sequence.stream().map(sedarim::sunder)), // distance matrix as in Kriging
        values);
  }

  @Override
  public Tensor apply(Tensor point) {
    return sedarim.sunder(point).dot(weights);
  }
}
