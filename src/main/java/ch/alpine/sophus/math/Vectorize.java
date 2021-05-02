// code by jph
package ch.alpine.sophus.math;

import java.util.concurrent.atomic.AtomicInteger;

import ch.alpine.tensor.Tensor;

public enum Vectorize {
  ;
  /** @param matrix
   * @param index for instance 0 to include diagonal elements, or -1 to exclude diagonal elements
   * @return vector
   * @throws Exception if given matrix is not a tensor of rank at least 2 */
  public static Tensor of(Tensor matrix, int index) {
    AtomicInteger atomicInteger = new AtomicInteger(index + 1);
    return Tensor.of(matrix.stream() //
        .flatMap(row -> row.stream().limit(atomicInteger.getAndIncrement())));
  }
}
