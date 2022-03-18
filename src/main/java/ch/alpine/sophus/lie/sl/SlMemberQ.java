// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;

public enum SlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor g) {
    return Tolerance.CHOP.isClose(Det.of(g), RealScalar.ONE);
  }
}
