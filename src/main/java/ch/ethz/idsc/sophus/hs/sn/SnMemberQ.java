// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.nrm.VectorNorm2;

public enum SnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    return Tolerance.CHOP.isClose(VectorNorm2.of(x), RealScalar.ONE);
  }
}
