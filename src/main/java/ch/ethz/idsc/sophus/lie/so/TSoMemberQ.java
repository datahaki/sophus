// code by jph
package ch.ethz.idsc.sophus.lie.so;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public enum TSoMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return AntisymmetricMatrixQ.of(v, Chop._08);
  }
}
