// code by jph
package ch.ethz.idsc.sophus.hs.st;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;

/** k x n matrix with k <= n */
public enum StMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return OrthogonalMatrixQ.of(p);
  }
}
