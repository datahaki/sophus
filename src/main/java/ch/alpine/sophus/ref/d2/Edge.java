// code by jph
package ch.alpine.sophus.ref.d2;

import java.io.Serializable;
import java.util.stream.IntStream;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

public record Edge(int i, int j) implements Serializable {
  /** @param tensor
   * @return tensor.Get(i, j) */
  public Scalar Get(Tensor tensor) {
    return tensor.Get(i, j);
  }

  public Edge reverse() {
    return new Edge(j, i);
  }

  public IntStream stream() {
    return IntStream.of(i, j);
  }

  public int[] array() {
    return new int[] { i, j };
  }
}
