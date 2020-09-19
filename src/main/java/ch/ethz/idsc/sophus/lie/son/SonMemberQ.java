// code by jph
package ch.ethz.idsc.sophus.lie.son;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class SonMemberQ extends AbstractMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static MemberQ of(Chop chop) {
    return new SonMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private SonMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    return chop.isClose(Det.of(x), RealScalar.ONE) //
        && OrthogonalMatrixQ.of(x, chop);
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor p, Tensor v) {
    return AntisymmetricMatrixQ.of(LinearSolve.of(p, v), chop);
  }
}
