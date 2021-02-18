// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.RationalScalar;
import ch.ethz.idsc.tensor.RealScalar;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.Drop;
import ch.ethz.idsc.tensor.alg.Last;
import ch.ethz.idsc.tensor.nrm.Vector2Norm;
import ch.ethz.idsc.tensor.sca.Chop;
import ch.ethz.idsc.tensor.sca.Sign;

/** for instance the point {0, 0, ..., 0, 1} with (n + 1) entries is member of H^n */
public enum HnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    Scalar xn = Last.of(p);
    return Sign.isPositive(xn) //
        && Chop._08.isClose(ratio(p), RationalScalar.ONE);
  }

  public static Scalar ratio(Tensor x) {
    Scalar xn = Last.of(x);
    Sign.requirePositive(xn);
    return Vector2Norm.of(Drop.tail(x, 1).append(RealScalar.ONE)).divide(xn);
  }
}
