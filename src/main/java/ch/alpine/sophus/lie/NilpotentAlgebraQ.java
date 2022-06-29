// code by jph
package ch.alpine.sophus.lie;

import java.util.stream.IntStream;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.mat.NilpotentMatrixQ;
import ch.alpine.tensor.mat.Tolerance;

/** Quote:
 * The Killing form of a nilpotent Lie algebra is 0.
 * 
 * Reference:
 * https://en.wikipedia.org/wiki/Nilpotent_Lie_algebra */
public enum NilpotentAlgebraQ {
  ;
  private static final int SIZE = 16;
  private static final Cache<Tensor, Boolean> CACHE = Cache.of(NilpotentAlgebraQ::build, SIZE);

  private static Boolean build(Tensor ad) {
    return Tolerance.CHOP.allZero(KillingForm.of(ad)) //
        // TODO SOPHUS ALG it is not clear whether this criteria is sufficient
        && IntStream.range(0, ad.length()) //
            .allMatch(i -> NilpotentMatrixQ.of(ad.get(Tensor.ALL, Tensor.ALL, i)));
  }

  /** @param ad
   * @return whether lie algebra defined by given ad-tensor is nilpotent */
  public static boolean of(Tensor ad) {
    return CACHE.apply(ad);
  }
}
