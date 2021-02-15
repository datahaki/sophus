// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.ScalarUnaryOperator;
import ch.ethz.idsc.tensor.nrm.Hypot;
import ch.ethz.idsc.tensor.red.Max;
import ch.ethz.idsc.tensor.sca.Abs;
import ch.ethz.idsc.tensor.sca.Sqrt;

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
