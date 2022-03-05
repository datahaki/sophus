// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.MatrixDotTranspose;
import ch.alpine.tensor.sca.Chop;

/** Reference: geomstats */
public class TStMemberQ implements MemberQ, Serializable {
  private final Tensor p;

  /** @param p */
  public TStMemberQ(Tensor p) {
    this.p = p;
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return AntisymmetricMatrixQ.of(MatrixDotTranspose.of(p, v), Chop._06);
  }
}
