// code by jph
package ch.ethz.idsc.sophus.hs.spd;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.PositiveDefiniteMatrixQ;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class SpdMemberQ extends AbstractMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static MemberQ of(Chop chop) {
    return new SpdMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private SpdMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    return SymmetricMatrixQ.of(x, chop) //
        && PositiveDefiniteMatrixQ.ofHermitian(x, chop);
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor x, Tensor v) {
    throw new UnsupportedOperationException(); // TODO;
  }
}
