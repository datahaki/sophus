// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

/* package */ enum StaticHelper {
  ;
  private static final Chop CHOP = Chop._10;

  public static Tensor requirePoint(Tensor x) {
    CHOP.requireClose(HnNorm.INSTANCE.norm(x), RealScalar.ONE.negate());
    return x;
  }

  public static Tensor requireTangent(Tensor x, Tensor v) {
    CHOP.requireZero(HnBilinearForm.between(x, v));
    return v;
  }
}
