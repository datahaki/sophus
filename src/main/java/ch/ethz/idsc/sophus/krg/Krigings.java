// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.math.WeightingInterface;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.qty.Quantity;

/** Reference:
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
public enum Krigings {
  ;
  /** @param weightingInterface
   * @param sequence
   * @param values vector or matrix
   * @param covariance
   * @return */
  public static Kriging of(WeightingInterface weightingInterface, Tensor sequence, Tensor values, Tensor covariance) {
    Tensor vardst = Tensor.of(sequence.stream().map(point -> weightingInterface.weights(sequence, point)));
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    Scalar one = Quantity.of(RealScalar.ONE, StaticHelper.uniqueUnit(matrix));
    matrix.stream().forEach(row -> row.append(one));
    int n = sequence.length();
    Tensor inverse = PseudoInverse.of(matrix.append(Tensors.vector(i -> i < n ? one : one.zero(), n + 1)));
    Tensor weights = inverse.dot(values.copy().append(values.get(0).map(Scalar::zero)));
    return new KrigingImpl(weightingInterface, sequence, one, weights, inverse);
  }

  /***************************************************/
  /** Gaussian process regression
   * 
   * @param weightingInterface
   * @param sequence
   * @param values vector or matrix
   * @param covariance symmetric matrix
   * @return */
  public static Kriging regression( //
      WeightingInterface weightingInterface, Tensor sequence, Tensor values, Tensor covariance) {
    return of(weightingInterface, sequence, values, covariance);
  }

  /** @param weightingInterface
   * @param sequence of points
   * @param values vector or matrix associated to points in given sequence
   * @return */
  public static Kriging interpolation(WeightingInterface weightingInterface, Tensor sequence, Tensor values) {
    int n = values.length();
    // TODO Array.zeros(n, n) is not generic!
    return regression(weightingInterface, sequence, values, Array.zeros(n, n));
  }

  /** uses unit vectors as target values
   * 
   * @param weightingInterface
   * @param sequence of points
   * @return */
  public static Kriging barycentric(WeightingInterface weightingInterface, Tensor sequence) {
    return interpolation(weightingInterface, sequence, IdentityMatrix.of(sequence.length()));
  }
}
