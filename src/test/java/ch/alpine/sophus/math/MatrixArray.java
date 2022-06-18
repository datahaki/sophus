// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;

public enum MatrixArray {
  ;
  /** @param tensor not necessarily with array structure
   * @return
   * @throws Exception if given tensor is not a list of vectors */
  public static Tensor[][] of(Tensor tensor) {
    return tensor.stream().map(MatrixArray::ofVector).toArray(Tensor[][]::new);
  }

  // helper function
  private static Tensor[] ofVector(Tensor vector) {
    return vector.stream().toArray(Tensor[]::new);
  }
}
