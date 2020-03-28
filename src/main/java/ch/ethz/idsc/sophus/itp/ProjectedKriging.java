// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
import ch.ethz.idsc.sophus.hs.HsProjection;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
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
public enum ProjectedKriging {
  ;
  /** Gaussian process regression
   * 
   * @param flattenLogManifold
   * @param variogram
   * @param sequence
   * @param values
   * @param covariance symmetric matrix
   * @return */
  public static Kriging regression( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, //
      Tensor sequence, Tensor values, Tensor covariance) {
    return Kriging.of( //
        new ProjectedPseudoDistances(new HsProjection(flattenLogManifold), variogram, sequence), //
        sequence, values, covariance);
  }

  /** @param flattenLogManifold to measure the length of the difference between two points
   * @param variogram
   * @param sequence of points
   * @param values associated to points in given sequence
   * @return */
  public static Kriging interpolation( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence, Tensor values) {
    int n = values.length();
    // TODO Array.zeros(n, n) is not generic!
    return regression(flattenLogManifold, variogram, sequence, values, Array.zeros(n, n));
  }

  /** @param flattenLogManifold to measure the length of the difference between two points
   * @param variogram
   * @param sequence of points
   * @return */
  public static Kriging barycentric( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
    return interpolation(flattenLogManifold, variogram, sequence, IdentityMatrix.of(sequence.length()));
  }
}
