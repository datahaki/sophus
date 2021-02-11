// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

public class THnMemberQ implements MemberQ, Serializable {
  private final Tensor x;

  public THnMemberQ(Tensor x) {
    this.x = Objects.requireNonNull(x);
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return Chop._06.isZero(LBilinearForm.between(x, v));
  }

  public Tensor project(Tensor v) {
    return v.add(x.multiply(LBilinearForm.between(x, v)));
  }
}
