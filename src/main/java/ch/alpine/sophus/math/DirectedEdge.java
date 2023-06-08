// code by jph
package ch.alpine.sophus.math;

import java.io.Serializable;

/** inspired by
 * <a href="https://reference.wolfram.com/language/ref/DirectedEdge.html">DirectedEdge</a> */
public record DirectedEdge<T>(T i, T j) implements Serializable {
  public DirectedEdge<T> reverse() {
    return new DirectedEdge<>(j, i);
  }
}
