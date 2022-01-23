// code by jph
package ch.alpine.sophus.lie;

import java.io.Serializable;
import java.util.function.BinaryOperator;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Array;
import ch.alpine.tensor.alg.VectorQ;

/** @param h estimate in G
 * @param g residual in G */
public record HsPair(Tensor h, Tensor g) implements Serializable {
  public static HsPair seed(Tensor g) {
    int dim_g = g.length();
    return new HsPair( //
        Array.zeros(dim_g), //
        VectorQ.requireLength(g, dim_g));
  }

  /** @param bch
   * @param hd
   * @return */
  public HsPair move(BinaryOperator<Tensor> bch, Tensor hd) {
    return new HsPair( //
        bch.apply(h, hd), // towards bch(g, h) == lift(m)
        bch.apply(g, hd)); // towards bch(lift(m), -h) == g
  }
}
