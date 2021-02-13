// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

public class TSnMemberQ implements MemberQ, Serializable {
  private static final Chop CHOP = Chop._06;
  // ---
  private final Tensor x;

  /** @param x point on S^n embedded in R^(n+1) */
  public TSnMemberQ(Tensor x) {
    this.x = Objects.requireNonNull(x);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    // verifies that v is orthogonal to base point x
    return CHOP.isZero((Scalar) x.dot(v)); // errors of up to 1E-9 are expected
  }

  /** projection is idempotent
   * 
   * @param v vector
   * @return the projection of given v to the plane orthogonal to the base point x */
  public Tensor project(Tensor v) {
    return v.add(x.multiply((Scalar) x.dot(v).negate()));
  }
}
