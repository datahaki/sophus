// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.chq.MemberQ;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.sca.Chop;

public enum TSoMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean isMember(Tensor v) {
    return new AntisymmetricMatrixQ(Chop._08).isMember(v);
  }
}
