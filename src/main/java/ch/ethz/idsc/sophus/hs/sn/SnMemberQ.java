// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;
import java.util.Objects;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Hypot;
import ch.ethz.idsc.tensor.sca.Chop;

public class SnMemberQ extends AbstractMemberQ implements Serializable {
  /** @param chop
   * @return */
  public static MemberQ of(Chop chop) {
    return new SnMemberQ(Objects.requireNonNull(chop));
  }

  /***************************************************/
  private final Chop chop;

  private SnMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    return chop.isClose(Hypot.ofVector(x), RealScalar.ONE);
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor x, Tensor v) {
    // verifies that v is orthogonal to base point x
    return chop.isZero(x.dot(v).Get()); // errors of up to 1E-9 are expected
  }
}
