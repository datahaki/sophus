// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.sca.Cosh;
import ch.ethz.idsc.tensor.sca.Sinh;

public class HnRay {
  private final Tensor x;

  /** @param x in H^n */
  public HnRay(Tensor x) {
    this.x = HnMemberQ.INSTANCE.require(x);
  }

  /** @param v with {@link HnNorm} == 1
   * @param t any real number
   * @return */
  public Tensor shoot(Tensor v, Scalar t) {
    new THnMemberQ(x).require(v);
    Tolerance.CHOP.requireClose(LBilinearForm.normSquared(v), RealScalar.ONE);
    return x.multiply(Cosh.FUNCTION.apply(t)).add(v.multiply(Sinh.FUNCTION.apply(t)));
  }
}
