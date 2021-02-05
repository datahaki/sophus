// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Hypot;

public enum SnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean isMember(Tensor x) {
    return Tolerance.CHOP.isClose(Hypot.ofVector(x), RealScalar.ONE);
  }
}
