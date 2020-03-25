// code by jph
package ch.ethz.idsc.sophus.itp;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.rn.RnNorm;
import ch.ethz.idsc.sophus.math.win.ProjectionInterface;
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
public class BiinvariantKriging implements Kriging, Serializable {
  /** @param variogram
   * @param projectionInterface to measure the length of the difference between two points
   * @param sequence of points
   * @param values associated to points in given sequence
   * @return */
  public static Kriging interpolation( //
      ScalarUnaryOperator variogram, ProjectionInterface projectionInterface, //
      Tensor sequence, Tensor values) {
    int n = values.length();
    // TODO Array.zeros(n, n) is not generic!
    return new BiinvariantKriging(variogram, projectionInterface, sequence, values, Array.zeros(n, n));
  }

  /** @param variogram
   * @param projectionInterface to measure the length of the difference between two points
   * @param sequence of points
   * @return */
  public static Kriging barycentric( //
      ScalarUnaryOperator variogram, ProjectionInterface projectionInterface, Tensor sequence) {
    return interpolation(variogram, projectionInterface, sequence, IdentityMatrix.of(sequence.length()));
  }

  /** Gaussian process regression
   * 
   * @param variogram
   * @param projectionInterface
   * @param sequence
   * @param values
   * @param covariance symmetric matrix
   * @return */
  public static Kriging regression( //
      ScalarUnaryOperator variogram, ProjectionInterface projectionInterface, //
      Tensor sequence, Tensor values, Tensor covariance) {
    return new BiinvariantKriging(variogram, projectionInterface, sequence, values, covariance);
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;
  private final ProjectionInterface projectionInterface;
  private final Tensor sequence;
  //
  private final Scalar one;
  /** symmetric matrix */
  private final Tensor inverse;
  private final Tensor weights;

  /** @param variogram
   * @param projectionInterface
   * @param sequence
   * @param values
   * @param covariance symmetric matrix */
  private BiinvariantKriging( //
      ScalarUnaryOperator variogram, ProjectionInterface projectionInterface, //
      Tensor sequence, Tensor values, Tensor covariance) {
    // used in function #pseudoDistances
    this.variogram = variogram;
    this.projectionInterface = projectionInterface;
    this.sequence = sequence;
    // ---
    Tensor vardst = Tensor.of(sequence.stream().map(this::pseudoDistances));
    Tensor matrix = vardst.subtract(SymmetricMatrixQ.require(covariance));
    one = Quantity.of(RealScalar.ONE, StaticHelper.unique(matrix));
    matrix.stream().forEach(row -> row.append(one));
    int n = sequence.length();
    inverse = PseudoInverse.of(matrix.append(Tensors.vector(i -> i < n ? one : one.zero(), n + 1)));
    weights = inverse.dot(values.copy().append(values.get(0).map(Scalar::zero)));
  }

  @Override // from Kriging
  public Tensor estimate(Tensor point) {
    return pseudoDistances(point).append(one).dot(weights);
  }

  @Override // from Kriging
  public Scalar variance(Tensor point) {
    Tensor y = pseudoDistances(point).append(one);
    return inverse.dot(y).dot(y).Get();
  }

  /** @param point
   * @return vector with sequence.length() + 1 entries */
  private Tensor pseudoDistances(Tensor point) {
    // biinvariant symmetric projection matrix with eigenvalues either 1 or 0
    Tensor projection = projectionInterface.projection(sequence, point);
    Tensor normalized = IdentityMatrix.of(sequence.length()).subtract(projection);
    return Tensor.of(normalized.stream().map(RnNorm.INSTANCE::norm).map(variogram));
  }
}
