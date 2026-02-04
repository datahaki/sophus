// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Outer;
import ch.alpine.tensor.mat.UpperEvaluation;

/** Hint:
 * for DistanceMatrix[X, Y] use Outer
 * 
 * <p>inspired by
 * <a href="https://reference.wolfram.com/language/ref/DistanceMatrix.html">DistanceMatrix</a>
 * 
 * @see Outer */
// TODO SOPHUS API not used in sophus
public enum DistanceMatrix {
  ;
  /** @param sequence of length n
   * @param tensorMetric
   * @return symmetric matrix of size n x n with zeros along the diagonal */
  public static Tensor of(Tensor sequence, TensorMetric tensorMetric) {
    return UpperEvaluation.of(sequence, sequence, tensorMetric::distance, s -> s);
  }
}
