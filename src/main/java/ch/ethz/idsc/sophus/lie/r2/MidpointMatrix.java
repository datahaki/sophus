// code by jph
package ch.ethz.idsc.sophus.lie.r2;

import java.util.function.Function;

import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Array;
import ch.ethz.idsc.tensor.ext.Cache;
import ch.ethz.idsc.tensor.io.Pretty;

/**
 * 
 */
public enum MidpointMatrix {
  ;
  private static final Function<Integer, Tensor> CACHE = Cache.of(MidpointMatrix::build, 32);

  /** @param n
   * @return */
  public static Tensor of(int n) {
    return CACHE.apply(n);
  }

  private static Tensor build(int n) {
    return Array.of(list -> Math.floorMod(list.get(1) - list.get(0), n) < 2 //
        ? RationalScalar.HALF
        : RealScalar.ZERO, n, n);
  }

  public static void main(String[] args) {
    System.out.println(Pretty.of(build(5)));
  }
}
