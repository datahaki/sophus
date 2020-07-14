// code by jph
package ch.ethz.idsc.sophus.math;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

public enum Vectorize {
  ;
  /** @param matrix
   * @param index for instance 0 to include diagonal elements, or -1 to exclude diagonal elements
   * @return vector */
  public static Tensor lt(Tensor matrix, int index) {
    Tensor vector = Tensors.reserve(numel(matrix, index));
    for (Tensor row : matrix)
      row.stream().limit(++index).forEach(vector::append);
    return vector;
  }

  /* package */ static int numel(Tensor matrix, int index) {
    int n = matrix.length() + index;
    return n * (n + 1) / 2;
  }
}
