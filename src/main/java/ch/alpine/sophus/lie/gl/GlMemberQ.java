// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;

public enum GlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor matrix) {
    return SquareMatrixQ.of(matrix) //
        && !Tolerance.CHOP.isZero(Det.of(matrix));
  }
}
