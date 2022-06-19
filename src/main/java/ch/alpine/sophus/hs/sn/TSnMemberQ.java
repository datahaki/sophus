// code by jph
package ch.alpine.sophus.hs.sn;

import java.io.Serializable;
import java.util.Objects;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.sca.Chop;

public class TSnMemberQ implements MemberQ, Serializable {
  private static final Chop CHOP = Chop._06;
  // ---
  private final Tensor p;

  /** @param p point on S^n embedded in R^(n+1) */
  public TSnMemberQ(Tensor p) {
    this.p = Objects.requireNonNull(p);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    // verifies that v is orthogonal to base point x
    return CHOP.isZero((Scalar) p.dot(v)); // errors of up to 1E-9 are expected
  }

  /** projection is idempotent
   * 
   * @param v vector
   * @return the projection of given v to the plane orthogonal to the base point x */
  public Tensor project(Tensor v) {
    return v.add(p.multiply((Scalar) p.dot(v).negate()));
  }
}
