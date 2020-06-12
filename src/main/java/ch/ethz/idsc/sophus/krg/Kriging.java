// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.qty.Quantity;

/** implementation of kriging for homogeneous spaces
 * 
 * Reference:
 * "Biinvariant Kriging on Lie Groups"
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
    // TODO Array.zeros(n, n) is not generic!
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
    Tensor vardst = SymmetricMatrixQ.require(Tensor.of(sequence.stream().map(tensorUnaryOperator)));
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    Scalar one = Quantity.of(RealScalar.ONE, StaticHelper.uniqueUnit(matrix));
    matrix.stream().forEach(row -> row.append(one));
    int n = sequence.length();
    Tensor inverse = PseudoInverse.of(matrix.append(Tensors.vector(i -> i < n ? one : one.zero(), n + 1)));
    Tensor weights = inverse.dot(values.copy().append(values.get(0).map(Scalar::zero)));
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

  /** @param point
   * @return estimate at given point */
  public Tensor estimate(Tensor point) {
    return tensorUnaryOperator.apply(point).append(one).dot(weights);
  }

  /** @param point
   * @return variance of estimate at given point */
  public Scalar variance(Tensor point) {
    Tensor y = tensorUnaryOperator.apply(point).append(one);
    return inverse.dot(y).dot(y).Get();
  }
}
