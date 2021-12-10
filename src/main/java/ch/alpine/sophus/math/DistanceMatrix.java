// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/DistanceMatrix.html">DistanceMatrix</a> */
public enum DistanceMatrix {
  ;
  /** @param sequence of length n
   * @param tensorMetric
   * @return symmetric matrix of size n x n with zeros along the diagonal */
  public static Tensor of(Tensor sequence, TensorMetric tensorMetric) {
    int n = sequence.length();
    Scalar[][] matrix = new Scalar[n][n];
    int i = -1;
    for (Tensor p : sequence) {
      for (int j = ++i; j < n; ++j)
        matrix[i][j] = matrix[j][i] = tensorMetric.distance(p, sequence.get(j));
      matrix[i][i] = tensorMetric.distance(p, p);
    }
    return Tensors.matrix(matrix);
  }

  /** @param x sequence of length n
   * @param y sequence of length m
   * @param tensorMetric
   * @return matrix of size n x m */
  public static Tensor of(Tensor x, Tensor y, TensorMetric tensorMetric) {
    return Tensor.of(x.stream() //
        .map(x_row -> Tensor.of(y.stream() //
            .map(y_row -> tensorMetric.distance(x_row, y_row)))));
  }
}
