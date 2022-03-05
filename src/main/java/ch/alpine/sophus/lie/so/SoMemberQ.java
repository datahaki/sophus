// code by jph
package ch.alpine.sophus.lie.so;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;

public enum SoMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    return Tolerance.CHOP.isClose(Det.of(x), RealScalar.ONE) //
        && OrthogonalMatrixQ.of(x);
  }
}
