// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.AntisymmetricMatrixQ;
import ch.ethz.idsc.tensor.mat.LinearSolve;
import ch.ethz.idsc.tensor.mat.OrthogonalMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class So3MemberQ extends AbstractMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static MemberQ of(Chop chop) {
    return new So3MemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private So3MemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    return x.length() == 3 //
        && OrthogonalMatrixQ.of(x, chop);
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor p, Tensor v) {
    return AntisymmetricMatrixQ.of(LinearSolve.of(p, v), chop);
  }
}
