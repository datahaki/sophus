// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.hs.AbstractMemberQ;
import ch.ethz.idsc.sophus.hs.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.red.Norm;
import ch.ethz.idsc.tensor.sca.Chop;

public class SnMemberQ extends AbstractMemberQ implements Serializable {
  public static MemberQ of(Chop chop) {
    return new SnMemberQ(chop);
  }

  /***************************************************/
  private final Chop chop;

  private SnMemberQ(Chop chop) {
    this.chop = chop;
  }

  @Override // from MemberQ
  public boolean isPoint(Tensor x) {
    return chop.isClose(Norm._2.ofVector(x), RealScalar.ONE);
  }

  @Override // from MemberQ
  public boolean isTangent(Tensor x, Tensor v) {
    // verifies that v is orthogonal to base point x
    return chop.isZero(x.dot(v).Get()); // errors of up to 1E-9 are expected
  }
}
