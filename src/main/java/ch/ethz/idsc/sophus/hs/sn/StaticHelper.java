// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;

/* package */ enum StaticHelper {
  ;
  private static final Chop CHOP = Chop._07;

  /** @param x
   * @return */
  public static Tensor requirePoint(Tensor x) {
    CHOP.requireClose(Norm._2.ofVector(x), RealScalar.ONE);
    return x;
  }

  /** @param x
   * @param v
   * @return */
  public static Tensor requireTangent(Tensor x, Tensor v) {
    // verifies that v is orthogonal to base point x
    CHOP.requireZero(x.dot(v).Get()); // errors of up to 1E-9 are expected
    return v;
  }
}
