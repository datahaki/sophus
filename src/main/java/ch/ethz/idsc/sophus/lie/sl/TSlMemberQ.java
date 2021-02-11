// code by jph
package ch.ethz.idsc.sophus.lie.sl;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.red.Trace;

public enum TSlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    return Tolerance.CHOP.isZero(Trace.of(x));
  }
}
