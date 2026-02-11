// code by jph
package ch.alpine.sophus.math.api;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;
import ch.alpine.tensor.sca.pow.Sqrt;

@FunctionalInterface
public interface BilinearForm {
  /** @param u
   * @param v
   * @return */
  Scalar formEval(Tensor u, Tensor v);

  /** @param u
   * @return */
  default Scalar normSquared(Tensor u) {
    return formEval(u, u);
  }

  /** @param u
   * @return */
  default Scalar norm(Tensor u) {
    Scalar n2 = normSquared(u);
    if (Sign.isNegative(n2)) {
      n2 = Chop._08.apply(n2);
      if (Sign.isNegative(n2)) {
        System.err.println("vector norm " + n2);
        // n2 = n2.zero();
        new Throw(u).printStackTrace();
      }
    }
    return Sqrt.FUNCTION.apply(n2);
  }
}
