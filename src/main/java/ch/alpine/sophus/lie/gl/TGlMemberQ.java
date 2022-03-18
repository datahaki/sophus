// code by jph
package ch.alpine.sophus.lie.gl;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.SquareMatrixQ;

public enum TGlMemberQ implements MemberQ {
  INSTANCE;

  @Override
  public boolean test(Tensor tensor) {
    return SquareMatrixQ.of(tensor);
  }
}
