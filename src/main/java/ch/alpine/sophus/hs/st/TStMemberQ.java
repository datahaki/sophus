// code by jph
package ch.alpine.sophus.hs.st;

import java.io.Serializable;

import ch.alpine.sophus.math.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.mat.AntisymmetricMatrixQ;
import ch.alpine.tensor.mat.MatrixDotTranspose;
import ch.alpine.tensor.sca.Chop;

/** Reference: geomstats */
public record TStMemberQ(Tensor p) implements MemberQ, Serializable {
  @Override // from MemberQ
  public boolean test(Tensor v) {
    return AntisymmetricMatrixQ.of(MatrixDotTranspose.of(p, v), Chop._06);
  }
}
