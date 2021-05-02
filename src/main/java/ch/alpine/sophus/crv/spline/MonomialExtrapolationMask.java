// code by jph
package ch.alpine.sophus.crv.spline;

import java.io.Serializable;
import java.util.function.Function;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.ext.Cache;
import ch.alpine.tensor.ext.Integers;
import ch.alpine.tensor.num.Binomial;

/** the weights follow from a linear system of equations with entries
 * LHS=x^k for x=-deg, ..., 0, and k = 0, ..., deg
 * RHS=1^k for k = 0, ..., deg
 * 
 * For instance, deg=3
 * LHS=
 * +1 +1 +1 +1 : x^0
 * -3 -2 -1 +0 : x^1
 * +9 +4 +1 +0 : x^2
 * -27 -8 -1 0 : x^3
 * 
 * RHS=[1 1 1 1]' */
public class MonomialExtrapolationMask implements Function<Integer, Tensor>, Serializable {
  public static final Function<Integer, Tensor> INSTANCE = //
      Cache.of(new MonomialExtrapolationMask(), 32);

  /***************************************************/
  private MonomialExtrapolationMask() {
    // ---
  }

  @Override
  public Tensor apply(Integer length) {
    Binomial binomial = Binomial.of(length);
    return Tensors.vector(k -> Integers.isEven(k + length) //
        ? binomial.over(k).negate()
        : binomial.over(k), length);
  }
}
