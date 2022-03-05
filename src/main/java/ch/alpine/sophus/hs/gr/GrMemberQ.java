// code by jph
package ch.alpine.sophus.hs.gr;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.ev.Eigensystem;
import ch.alpine.tensor.mat.gr.InfluenceMatrixQ;
import ch.alpine.tensor.sca.Chop;

/** rank can be determined via {@link Eigensystem} */
public enum GrMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return InfluenceMatrixQ.of(p, Chop._08); // 1e-10 does not always work
  }
}
