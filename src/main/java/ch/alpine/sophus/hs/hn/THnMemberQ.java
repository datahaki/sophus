// code by jph
package ch.alpine.sophus.hs.hn;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

public class THnMemberQ implements MemberQ, Serializable {
  private static final Chop CHOP = Chop._06;
  // ---
  private final Tensor p;

  public THnMemberQ(Tensor p) {
    this.p = Objects.requireNonNull(p);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return CHOP.isZero(LBilinearForm.between(p, v));
  }

  /** projection is idempotent
   * 
   * @param v
   * @return projection of v to plane orthogonal to base point x */
  public Tensor project(Tensor v) {
    return v.add(p.multiply(LBilinearForm.between(p, v)));
  }
}
