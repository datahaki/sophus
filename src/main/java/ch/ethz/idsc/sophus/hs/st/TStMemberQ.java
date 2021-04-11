// code by jph
package ch.ethz.idsc.sophus.hs.st;

import java.io.Serializable;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.MatrixDotTranspose;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

/** Reference: geomstats */
public class TStMemberQ implements MemberQ, Serializable {
  private final Tensor x;

  /** @param x */
  public TStMemberQ(Tensor x) {
    this.x = x;
  }

  @Override // from MemberQ
  public boolean test(Tensor v) {
    return AntisymmetricMatrixQ.of(MatrixDotTranspose.of(x, v), Chop._06);
  }
}
