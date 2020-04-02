// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.qty.Quantity;

/** Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction, also known in different communities as Gauss-Markov estimation
 * or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007 */
public interface Kriging {
  /** @param pseudoDistances
   * @param sequence
   * @param values
   * @param covariance
   * @return */
  static Kriging of(PseudoDistances pseudoDistances, Tensor sequence, Tensor values, Tensor covariance) {
    Tensor vardst = Tensor.of(sequence.stream().map(pseudoDistances::pseudoDistances));
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    Scalar one = Quantity.of(RealScalar.ONE, StaticHelper.uniqueUnit(matrix));
    matrix.stream().forEach(row -> row.append(one));
    int n = sequence.length();
    Tensor inverse = PseudoInverse.of(matrix.append(Tensors.vector(i -> i < n ? one : one.zero(), n + 1)));
    Tensor weights = inverse.dot(values.copy().append(values.get(0).map(Scalar::zero)));
    return new KrigingImpl(pseudoDistances, one, weights, inverse);
  }

  /***************************************************/
  /** @param point
   * @return estimate at given point */
  Tensor estimate(Tensor point);

  /** @param point
   * @return variance of estimate at given point */
  Scalar variance(Tensor point);
}
