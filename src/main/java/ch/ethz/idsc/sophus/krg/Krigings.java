// code by jph
package ch.ethz.idsc.sophus.krg;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.PseudoInverse;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.qty.Quantity;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

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
 * @see ExponentialVariogram
 * @see PowerVariogram */
public enum Krigings {
  LOGNORM {
    @Override
    public PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new LognormPseudoDistances(flattenLogManifold, variogram, sequence);
    }
  },
  PROJECT {
    @Override
    public PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new ProjectPseudoDistances(flattenLogManifold, variogram, sequence);
    }
  };

  /** Gaussian process regression
   * 
   * @param flattenLogManifold
   * @param variogram
   * @param sequence
   * @param values
   * @param covariance symmetric matrix
   * @return */
  public Kriging regression( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, //
      Tensor sequence, Tensor values, Tensor covariance) {
    PseudoDistances pseudoDistances = pseudoDistances(flattenLogManifold, variogram, sequence);
    Tensor vardst = Tensor.of(sequence.stream().map(pseudoDistances::pseudoDistances));
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    Scalar one = Quantity.of(RealScalar.ONE, StaticHelper.uniqueUnit(matrix));
    matrix.stream().forEach(row -> row.append(one));
    int n = sequence.length();
    Tensor inverse = PseudoInverse.of(matrix.append(Tensors.vector(i -> i < n ? one : one.zero(), n + 1)));
    Tensor weights = inverse.dot(values.copy().append(values.get(0).map(Scalar::zero)));
    return new KrigingImpl(pseudoDistances, one, weights, inverse);
  }

  /** @param flattenLogManifold to measure the length of the difference between two points
   * @param variogram
   * @param sequence of points
   * @param values associated to points in given sequence
   * @return */
  public Kriging interpolation( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence, Tensor values) {
    int n = values.length();
    // TODO Array.zeros(n, n) is not generic!
    return regression(flattenLogManifold, variogram, sequence, values, Array.zeros(n, n));
  }

  /** uses unit vectors as target values
   * 
   * @param flattenLogManifold to measure the length of the difference between two points
   * @param variogram
   * @param sequence of points
   * @return */
  public Kriging barycentric( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return interpolation(flattenLogManifold, variogram, sequence, IdentityMatrix.of(sequence.length()));
  }

  /** @param flattenLogManifold
   * @param variogram
   * @param sequence
   * @return */
  public abstract PseudoDistances pseudoDistances( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence);
}
