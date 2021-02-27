// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

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
