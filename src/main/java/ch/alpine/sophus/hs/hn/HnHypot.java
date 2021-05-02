// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Scalars;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.ScalarUnaryOperator;
import ch.alpine.tensor.nrm.Hypot;
import ch.alpine.tensor.red.Max;
import ch.alpine.tensor.sca.Abs;
import ch.alpine.tensor.sca.Sqrt;

/** @see Hypot */
public enum HnHypot {
  ;
  public static Scalar of(Tensor x, ScalarUnaryOperator suo) {
    Tensor abs = x.map(Abs.FUNCTION);
    Scalar max = (Scalar) abs.stream().reduce(Max::of).get();
    if (Scalars.isZero(max))
      return max;
    abs = abs.divide(max);
    Scalar n2 = LBilinearForm.normSquared(abs);
    return max.multiply(Sqrt.FUNCTION.apply(suo.apply(n2)));
  }
}
