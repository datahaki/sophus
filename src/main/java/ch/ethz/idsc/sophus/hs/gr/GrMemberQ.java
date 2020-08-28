// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

public class GrMemberQ extends AbstractMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static MemberQ of(Chop chop) {
    return new GrMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private GrMemberQ(Chop chop) {
    this.chop = chop;
  }

  /** @param x
   * @param chop
   * @return */
  @Override
  public boolean isPoint(Tensor x) {
    return GrassmannQ.of(x, chop);
  }

  @Override
  public boolean isTangent(Tensor x, Tensor v) {
    return chop.isClose(x.dot(v).add(v.dot(x)), v);
  }
}
