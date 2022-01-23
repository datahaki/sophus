// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;

/** @param g residual in G
 * @param h estimate in G */
public record HsPair(Tensor g, Tensor h) implements Serializable {
  public static HsPair seed(Tensor g) {
    int dim_g = g.length();
    return new HsPair( //
        VectorQ.requireLength(g, dim_g), //
        Array.zeros(dim_g));
  }

  /** @param bch
   * @param hd
   * @return */
  public HsPair move(BinaryOperator<Tensor> bch, Tensor hd) {
    return new HsPair( //
        bch.apply(g, hd), // towards bch(lift(m), -h) == g
        bch.apply(h, hd)); // towards bch(g, h) == lift(m)
  }
}
