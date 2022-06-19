// code by jph
package ch.alpine.sophus.lie.sl;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.red.Trace;

public enum TSlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    return Tolerance.CHOP.isZero(Trace.of(x));
  }
}
