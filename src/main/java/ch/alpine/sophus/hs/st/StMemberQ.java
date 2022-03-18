// code by jph
package ch.alpine.sophus.hs.st;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;
import ch.alpine.tensor.sca.Chop;

/** k x n matrix with k <= n */
public enum StMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return OrthogonalMatrixQ.of(p, Chop._10); // 1e-12 does not always work
  }
}
