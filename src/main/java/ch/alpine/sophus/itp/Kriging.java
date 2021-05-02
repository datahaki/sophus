// code by jph
package ch.alpine.sophus.itp;

import java.io.Serializable;

import ch.alpine.sophus.math.LagrangeMultiplier;
import ch.alpine.sophus.math.var.ExponentialVariogram;
import ch.alpine.sophus.math.var.PowerVariogram;
import ch.alpine.sophus.math.var.SphericalVariogram;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.ConstantArray;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.IdentityMatrix;
import ch.alpine.tensor.mat.PseudoInverse;
import ch.alpine.tensor.mat.SymmetricMatrixQ;
import ch.alpine.tensor.qty.Quantity;

/** implementation of kriging for homogeneous spaces
 * 
 * Reference:
 * "Biinvariant Distance Vectors"
 * by Jan Hakenberg, 2020
 * 
 * <p>Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction, also known in different communities as Gauss-Markov estimation
 * or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 
 * @see PowerVariogram
 * @see ExponentialVariogram
 * @see SphericalVariogram */
public class Kriging implements Serializable {
  /** Gaussian process regression
   * 
   * @param tensorUnaryOperator
   * @param sequence
   * @param values vector or matrix
   * @param covariance symmetric matrix
   * @return */
  public static Kriging regression( //
      TensorUnaryOperator tensorUnaryOperator, Tensor sequence, Tensor values, Tensor covariance) {
    return of(tensorUnaryOperator, sequence, values, covariance);
  }

  /** @param tensorUnaryOperator
   * @param sequence of points
   * @param values vector or matrix associated to points in given sequence
   * @return */
  public static Kriging interpolation(TensorUnaryOperator tensorUnaryOperator, Tensor sequence, Tensor values) {
    int n = values.length();
    // Array.zeros(n, n) may not be sufficiently generic
    return regression(tensorUnaryOperator, sequence, values, Array.zeros(n, n));
  }

  /** uses unit vectors as target values
   * 
   * @param tensorUnaryOperator
   * @param sequence of points
   * @return */
  public static Kriging barycentric(TensorUnaryOperator tensorUnaryOperator, Tensor sequence) {
    return interpolation( //
        tensorUnaryOperator, //
        sequence, //
        IdentityMatrix.of(sequence.length()) // unit vectors as values
    );
  }

  /** @param tensorUnaryOperator
   * @param sequence
   * @param values vector or matrix
   * @param covariance
   * @return */
  public static Kriging of( //
      TensorUnaryOperator tensorUnaryOperator, Tensor sequence, Tensor values, Tensor covariance) {
    // symmetric distance matrix eq (3.7.13)
    Tensor vardst = Tensor.of(sequence.stream().map(tensorUnaryOperator));
    SymmetricMatrixQ.require(vardst);
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    Scalar one = Quantity.of(RealScalar.ONE, StaticHelper.uniqueUnit(matrix));
    int n = matrix.length();
    Tensor rhs = Tensors.of(values.get(0).map(Scalar::zero));
    LagrangeMultiplier lagrangeMultiplier = //
        new LagrangeMultiplier(matrix, values, ConstantArray.of(one, 1, n), rhs);
    Tensor inverse = PseudoInverse.of(lagrangeMultiplier.matrix());
    Tensor weights = inverse.dot(lagrangeMultiplier.b());
    return new Kriging(tensorUnaryOperator, one, weights, inverse);
  }

  /***************************************************/
  private final TensorUnaryOperator tensorUnaryOperator;
  private final Scalar one;
  private final Tensor weights;
  private final Tensor inverse;

  private Kriging(TensorUnaryOperator tensorUnaryOperator, Scalar one, Tensor weights, Tensor inverse) {
    this.tensorUnaryOperator = tensorUnaryOperator;
    this.one = one;
    this.weights = weights;
    this.inverse = inverse;
  }

  private Tensor vs(Tensor point) {
    return tensorUnaryOperator.apply(point).append(one);
  }

  /** @param point
   * @return estimate at given point */
  public Tensor estimate(Tensor point) {
    return vs(point).dot(weights);
  }

  /** @param point
   * @return variance of estimate at given point */
  public Scalar variance(Tensor point) {
    Tensor vs = vs(point);
    return (Scalar) inverse.dot(vs).dot(vs);
  }
}
