// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;

public class TSpdMemberQ implements MemberQ, Serializable {
  private final Tensor x;

  public TSpdMemberQ(Tensor x) {
    this.x = Objects.requireNonNull(x);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    // TODO tangent check should be simple in SPD
    throw new UnsupportedOperationException();
  }
}
