// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.sca.Chop;

public class HnMemberQ extends AbstractMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static MemberQ of(Chop chop) {
    return new HnMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private HnMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    Scalar xn = Last.of(x);
    if (Scalars.lessEquals(RealScalar.ONE, xn))
      return chop.isClose(HnNormSquared.INSTANCE.norm(x), RealScalar.ONE.negate());
    return false;
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor x, Tensor v) {
    return chop.isZero(HnBilinearForm.between(x, v));
  }
}
