// code by jph
package ch.alpine.sophus.hs.spd;

import ch.alpine.sophus.math.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.PositiveDefiniteMatrixQ;
import ch.alpine.tensor.mat.SymmetricMatrixQ;

public enum SpdMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return SymmetricMatrixQ.of(p) //
        && PositiveDefiniteMatrixQ.ofHermitian(p);
  }
}
