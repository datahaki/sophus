// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

public class TSnMemberQ implements MemberQ, Serializable {
  private final Tensor x;

  public TSnMemberQ(Tensor x) {
    this.x = x;
  }

  @Override // from MemberQ
  public boolean isMember(Tensor v) {
    // verifies that v is orthogonal to base point x
    return Chop._06.isZero((Scalar) x.dot(v)); // errors of up to 1E-9 are expected
  }
}
