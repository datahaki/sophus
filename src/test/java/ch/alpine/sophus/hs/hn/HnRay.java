// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.sca.tri.Cosh;
import ch.alpine.tensor.sca.tri.Sinh;

public class HnRay {
  private final Tensor x;

  /** @param x in H^n */
  public HnRay(Tensor x) {
    this.x = HnMemberQ.INSTANCE.require(x);
  }

  /** @param v with {@link HnVectorNorm} == 1
   * @param t any real number
   * @return */
  public Tensor shoot(Tensor v, Scalar t) {
    new THnMemberQ(x).require(v);
    Tolerance.CHOP.requireClose(LBilinearForm.normSquared(v), RealScalar.ONE);
    return x.multiply(Cosh.FUNCTION.apply(t)).add(v.multiply(Sinh.FUNCTION.apply(t)));
  }
}
