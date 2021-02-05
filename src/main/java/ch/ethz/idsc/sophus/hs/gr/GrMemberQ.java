// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractHsMemberQ;
import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.mat.SymmetricMatrixQ;
import ch.ethz.idsc.tensor.sca.Chop;

public class GrMemberQ extends AbstractHsMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static HsMemberQ of(Chop chop) {
    return new GrMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private GrMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    return GrassmannQ.of(x, chop);
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor x, Tensor v) {
    return SymmetricMatrixQ.of(v, chop) //
        && chop.isClose(x.dot(v).add(v.dot(x)), v);
  }
}
