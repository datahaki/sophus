// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.math.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.Tolerance;
import ch.alpine.tensor.mat.re.Det;

public enum GlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor matrix) {
    return !Tolerance.CHOP.isZero(Det.of(matrix));
  }
}
