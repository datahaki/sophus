// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/UndirectedEdge.html">UndirectedEdge</a> */
public record UndirectedEdge(int i, int j) implements Serializable {
  public UndirectedEdge {
    if (j < i) {
      int k = i;
      i = j;
      j = k;
    }
  }

  /** @param tensor
   * @return tensor.Get(i, j) */
  public Scalar Get(Tensor tensor) {
    return tensor.Get(i, j);
  }
}
