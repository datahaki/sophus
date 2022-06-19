// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

public enum TSoMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return AntisymmetricMatrixQ.of(v, Chop._08);
  }
}
