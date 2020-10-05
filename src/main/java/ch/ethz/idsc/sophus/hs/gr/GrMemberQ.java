// code by jph
package ch.ethz.idsc.sophus.hs.gr;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractHsMemberQ;
import ch.ethz.idsc.sophus.hs.HsMemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.sca.Chop;

public class GrMemberQ extends AbstractHsMemberQ implements Serializable {
  private static final long serialVersionUID = 8544347081121190371L;

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
    return chop.isClose(x.dot(v).add(v.dot(x)), v);
  }
}
