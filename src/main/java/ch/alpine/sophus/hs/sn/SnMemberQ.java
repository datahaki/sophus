// code by jph
package ch.alpine.sophus.hs.sn;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.nrm.Vector2Norm;

public enum SnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return Tolerance.CHOP.isClose(Vector2Norm.of(p), RealScalar.ONE);
  }
}
