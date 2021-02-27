// code by jph
package ch.ethz.idsc.sophus.lie.gl;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.SquareMatrixQ;

public enum TGlMemberQ implements MemberQ {
  INSTANCE;

  @Override
  public boolean test(Tensor tensor) {
    return SquareMatrixQ.of(tensor);
  }
}
