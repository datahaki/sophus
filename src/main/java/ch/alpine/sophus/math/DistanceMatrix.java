// code by jph
package ch.alpine.sophus.math;

import ch.alpine.sophus.api.TensorMetric;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.UpperEvaluation;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/DistanceMatrix.html">DistanceMatrix</a> */
public enum DistanceMatrix {
  ;
  /** @param sequence of length n
   * @param tensorMetric
   * @return symmetric matrix of size n x n with zeros along the diagonal */
  public static Tensor of(Tensor sequence, TensorMetric tensorMetric) {
    return UpperEvaluation.of(sequence, sequence, tensorMetric::distance, s -> s);
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
