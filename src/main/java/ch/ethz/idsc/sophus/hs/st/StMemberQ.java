// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;

public enum StMemberQ implements MemberQ {
  INSTANCE;

  @Override
  public boolean test(Tensor x) {
    // FIXME
    return OrthogonalMatrixQ.of(x);
  }
}
