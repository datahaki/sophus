// code by jph
package ch.ethz.idsc.sophus.krg;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.FlattenLogManifold;
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
 * @see PowerVariogram
 * @see ExponentialVariogram
 * @see SphericalVariogram */
public enum Krigings {
  ABSOLUTE {
    @Override
    PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new AbsoluteDistances(flattenLogManifold, variogram, sequence);
    }
  },
  RELATIVE {
    @Override
    PseudoDistances pseudoDistances(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence) {
      return new RelativeDistances(flattenLogManifold, variogram, sequence);
    }
  };

  /** @param pseudoDistances
   * @param sequence
   * @param values vector or matrix
   * @param covariance
   * @return */
  public static Kriging of(PseudoDistances pseudoDistances, Tensor sequence, Tensor values, Tensor covariance) {
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
  /** Gaussian process regression
   * 
   * @param flattenLogManifold
   * @param variogram
   * @param sequence
   * @param values vector or matrix
   * @param covariance symmetric matrix
   * @return */
  public Kriging regression( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, //
      Tensor sequence, Tensor values, Tensor covariance) {
    return of(pseudoDistances(flattenLogManifold, variogram, sequence), sequence, values, covariance);
  }

  /** @param flattenLogManifold to measure the length of the difference between two points
   * @param variogram
   * @param sequence of points
   * @param values vector or matrix associated to points in given sequence
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

  /***************************************************/
  /** Careful: Every evaluation of returned WeightingInterface is expensive!
   * If multiple evaluations are required for the same sequence, then use
   * {@link #barycentric(FlattenLogManifold, ScalarUnaryOperator, Tensor)}
   * 
   * @param flattenLogManifold
   * @param variogram
   * @return */
  public WeightingInterface weighting(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
    return new WeightingImpl(Objects.requireNonNull(flattenLogManifold), Objects.requireNonNull(variogram));
  }

  /* package */ abstract PseudoDistances pseudoDistances( //
      FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram, Tensor sequence);

  /***************************************************/
  private class WeightingImpl implements WeightingInterface, Serializable {
    private final FlattenLogManifold flattenLogManifold;
    private final ScalarUnaryOperator variogram;

    public WeightingImpl(FlattenLogManifold flattenLogManifold, ScalarUnaryOperator variogram) {
      this.flattenLogManifold = flattenLogManifold;
      this.variogram = variogram;
    }

    @Override
    public Tensor weights(Tensor sequence, Tensor point) {
      return barycentric(flattenLogManifold, variogram, sequence).estimate(point);
    }
  }
}
