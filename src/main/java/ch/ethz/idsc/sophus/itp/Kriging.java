// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.math.TensorNorm;
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
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction (13.6), also known in different communities as Gauss-Markov
 * estimation or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 
 * @see ExponentialVariogram
 * @see PowerVariogram */
public class Kriging implements TensorUnaryOperator {
  /** @param variogram
   * @param tensorNorm to measure the length of the difference between two points
   * @param sequence of points
   * @param values associated to points in given sequence
   * @return */
  public static Kriging interpolation( //
      ScalarUnaryOperator variogram, TensorNorm tensorNorm, //
      Tensor sequence, Tensor values) {
    int n = values.length();
    // TODO Array.zeros(n, n) is not generic!
    return new Kriging(variogram, tensorNorm, sequence, values, Array.zeros(n, n));
  }

  /** @param variogram
   * @param tensorNorm to measure the length of the difference between two points
   * @param sequence of points
   * @return */
  public static Kriging barycentric(ScalarUnaryOperator variogram, TensorNorm tensorNorm, Tensor sequence) {
    return interpolation(variogram, tensorNorm, sequence, IdentityMatrix.of(sequence.length()));
  }

  /** Gaussian process regression
   * 
   * @param variogram
   * @param tensorNorm
   * @param sequence
   * @param values
   * @param covariance symmetric matrix
   * @return */
  public static Kriging regression( //
      ScalarUnaryOperator variogram, TensorNorm tensorNorm, //
      Tensor sequence, Tensor values, Tensor covariance) {
    return new Kriging(variogram, tensorNorm, sequence, values, covariance);
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;
  private final TensorNorm tensorNorm;
  private final Tensor sequence;
  //
  private final Scalar one;
  /** symmetric matrix */
  private final Tensor inverse;
  private final Tensor weights;

  /** @param variogram
   * @param tensorNorm
   * @param sequence
   * @param values
   * @param covariance symmetric matrix */
  private Kriging(ScalarUnaryOperator variogram, TensorNorm tensorNorm, Tensor sequence, Tensor values, Tensor covariance) {
    // used in function #distances
    this.variogram = variogram;
    this.tensorNorm = tensorNorm;
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

  /** @param x
   * @return error estimate */
  public Scalar variance(Tensor x) {
    Tensor y = pseudoDistances(x).append(one);
    return inverse.dot(y).dot(y).Get();
  }

  /** @param x
   * @return vector with sequence.length() + 1 entries */
  private Tensor pseudoDistances(Tensor x) {
    return Tensor.of(sequence.stream() //
        .map(x::subtract) //
        .map(tensorNorm::norm) //
        .map(variogram));
  }
}
