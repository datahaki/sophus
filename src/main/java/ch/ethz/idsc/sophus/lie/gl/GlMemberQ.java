// code by jph
package ch.ethz.idsc.sophus.lie.gl;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.Tolerance;
import ch.ethz.idsc.tensor.mat.re.Det;

public enum GlMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor matrix) {
    return !Tolerance.CHOP.isZero(Det.of(matrix));
  }
}
