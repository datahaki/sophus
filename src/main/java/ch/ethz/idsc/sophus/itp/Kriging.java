// code by jph
package ch.ethz.idsc.sophus.itp;

import ch.ethz.idsc.sophus.math.TensorNorm;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.mat.DiagonalMatrix;
import ch.ethz.idsc.tensor.mat.IdentityMatrix;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;
import ch.ethz.idsc.tensor.qty.Boole;
import ch.ethz.idsc.tensor.sca.ScalarUnaryOperator;

/** Quote:
 * "Kriging is a technique named for South African mining engineer D.G. Krige. It is basically
 * a form of linear prediction (13.6), also known in different communities as Gauss-Markov
 * estimation or Gaussian process regression."
 * 
 * <p>Reference:
 * "Interpolation on Scattered Data in Multidimensions" in NR, 2007
 * 
 * @see PowerVariogram
 * @see ExponentialVariogram */
public class Kriging implements TensorUnaryOperator {
  /** @param variogram
   * @param tensorNorm to measure the length of the difference between two points
   * @param sequence of points
   * @param values associated to points in given sequence
   * @return */
  public static TensorUnaryOperator interpolation(ScalarUnaryOperator variogram, TensorNorm tensorNorm, Tensor sequence, Tensor values) {
    int n = values.length();
    return new Kriging(variogram, tensorNorm, sequence, values, Array.zeros(n, n));
  }

  /** @param variogram
   * @param tensorNorm to measure the length of the difference between two points
   * @param sequence of points
   * @return */
  public static TensorUnaryOperator barycentric(ScalarUnaryOperator variogram, TensorNorm tensorNorm, Tensor sequence) {
    return interpolation(variogram, tensorNorm, sequence, IdentityMatrix.of(sequence.length()));
  }

  /** Gaussian process regression
   * 
   * @param variogram
   * @param tensorNorm
   * @param sequence
   * @param values
   * @param values_error
   * @return */
  public static TensorUnaryOperator regression(ScalarUnaryOperator variogram, TensorNorm tensorNorm, Tensor sequence, Tensor values, Tensor values_error) {
    return new Kriging(variogram, tensorNorm, sequence, values, DiagonalMatrix.with(values_error));
  }

  /***************************************************/
  private final ScalarUnaryOperator variogram;
  private final TensorNorm tensorNorm;
  private final Tensor sequence;
  private final Tensor weights;

  /** @param variogram
   * @param tensorNorm
   * @param sequence
   * @param values
   * @param covariance */
  private Kriging(ScalarUnaryOperator variogram, TensorNorm tensorNorm, Tensor sequence, Tensor values, Tensor covariance) {
    // used in function #distances
    this.variogram = variogram;
    this.tensorNorm = tensorNorm;
    this.sequence = sequence;
    // ---
    Tensor matrix = Tensor.of(sequence.stream().map(this::distances)).subtract(covariance);
    matrix.stream().forEach(tensor -> tensor.append(RealScalar.ONE));
    int n = sequence.length();
    matrix.append(Tensors.vector(i -> Boole.of(i < n), n + 1));
    weights = LinearSolve.of(matrix, values.copy().append(values.get(0).map(Scalar::zero)));
  }

  @Override
  public Tensor apply(Tensor x) {
    return distances(x).append(RealScalar.ONE).dot(weights);
  }

  /** @param x
   * @return vector with sequence.length() + 1 entries */
  private Tensor distances(Tensor x) {
    return Tensor.of(sequence.stream() //
        .map(x::subtract) //
        .map(tensorNorm::norm) //
        .map(variogram));
  }
}
