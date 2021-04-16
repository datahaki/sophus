// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.ev.Eigensystem;
import ch.ethz.idsc.tensor.mat.gr.InfluenceMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

/** rank can be determined via {@link Eigensystem} */
public enum GrMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    return InfluenceMatrixQ.of(p, Chop._10);
  }
}
