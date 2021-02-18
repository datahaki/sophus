// code by jph
package ch.ethz.idsc.sophus.lie.sl;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.Tolerance;

public enum SlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor g) {
    return Tolerance.CHOP.isClose(Det.of(g), RealScalar.ONE);
  }
}
