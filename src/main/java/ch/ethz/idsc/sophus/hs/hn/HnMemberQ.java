// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Scalars;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.sca.Chop;

public enum HnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    Scalar xn = Last.of(x);
    return Scalars.lessEquals(RealScalar.ONE, xn) //
        ? Chop._08.isClose(HnNormSquared.INSTANCE.norm(x), RealScalar.ONE.negate())
        : false;
  }
}
