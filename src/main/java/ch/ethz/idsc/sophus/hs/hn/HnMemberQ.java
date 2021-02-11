// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;

/** for instance the point {0, 0, ..., 0, 1} with (n + 1) entries is member of H^n */
public enum HnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor x) {
    Scalar xn = Last.of(x);
    return Sign.isPositive(xn) //
        ? Chop._08.isClose(LBilinearForm.between(x, x), RealScalar.ONE.negate())
        : false;
  }
}
