// code by jph
package ch.alpine.sophus.math;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Int;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.mat.SquareMatrixQ;

public enum UpperVectorize {
  ;
  /** @param tensor with rank at least 2
   * @param index for instance 0 to include diagonal elements, or -1 to exclude diagonal elements
   * @return vector
   * @throws Exception if given tensor is not a tensor of rank at least 2 */
  public static Tensor of(Tensor tensor, int index) {
    SquareMatrixQ.INSTANCE.require(tensor);
    Int i = new Int(Integers.requirePositiveOrZero(index));
    return Tensor.of(tensor.stream() //
        .flatMap(row -> row.stream().skip(i.getAndIncrement())));
  }
}
