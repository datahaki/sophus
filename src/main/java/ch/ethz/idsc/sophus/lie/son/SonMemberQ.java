// code by jph
package ch.ethz.idsc.sophus.lie.son;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractHsMemberQ;
import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.Det;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class SonMemberQ extends AbstractHsMemberQ implements Serializable {
  private static final long serialVersionUID = 7549561191947505799L;

  /** @param chop
   * @return */
  public static HsMemberQ of(Chop chop) {
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
