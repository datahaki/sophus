// code by jph
package ch.ethz.idsc.sophus.lie.son;

import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.mat.Tolerance;

public enum SonMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean isMember(Tensor x) {
    return Tolerance.CHOP.isClose(Det.of(x), RealScalar.ONE) //
        && OrthogonalMatrixQ.of(x);
  }
  //
  // @Override // from MemberQ
  // public boolean isTangent(Tensor p, Tensor v) {
  // return AntisymmetricMatrixQ.of(LinearSolve.of(p, v), chop);
  // }
}
