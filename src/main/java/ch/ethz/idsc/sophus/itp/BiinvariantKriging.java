// code by jph
package ch.ethz.idsc.sophus.itp;

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

/** @see ExponentialVariogram
 * @see PowerVariogram */
public class BiinvariantKriging implements Kriging {
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

  @Override
  public Tensor apply(Tensor x) {
    return pseudoDistances(x).append(one).dot(weights);
  }

  @Override
  public Scalar variance(Tensor x) {
    Tensor y = pseudoDistances(x).append(one);
    return inverse.dot(y).dot(y).Get();
  }

  /** @param x
   * @return vector with sequence.length() + 1 entries */
  private Tensor pseudoDistances(Tensor x) {
    // biinvariant symmetric projection matrix with eigenvalues either 1 or 0
    Tensor projection = projectionInterface.projection(sequence, x);
    Tensor normalized = IdentityMatrix.of(sequence.length()).subtract(projection);
    return Tensor.of(normalized.stream().map(RnNorm.INSTANCE::norm).map(variogram));
  }
}
