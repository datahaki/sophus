// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;

public enum GrMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean isMember(Tensor x) {
    return GrassmannQ.of(x, Tolerance.CHOP);
  }
}
