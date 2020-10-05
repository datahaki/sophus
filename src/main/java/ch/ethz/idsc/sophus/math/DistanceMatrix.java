// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/DistanceMatrix.html">DistanceMatrix</a> */
public enum DistanceMatrix {
  ;
  /** @param sequence of length n
   * @param tensorMetric
   * @return symmetric matrix of size n x n with zeros along the diagonal */
  public static Tensor of(Tensor sequence, TensorMetric tensorMetric) {
    int n = sequence.length();
    Tensor matrix = Array.zeros(n, n);
    int i = -1;
    for (Tensor p : sequence)
      for (int j = ++i; j < n; ++j) {
        Tensor q = sequence.get(j);
        Scalar distance = tensorMetric.distance(p, q);
        matrix.set(distance, i, j);
        matrix.set(distance, j, i);
      }
    return matrix;
  }
}
