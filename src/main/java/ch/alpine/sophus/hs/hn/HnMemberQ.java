// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.RealScalar;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Drop;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.nrm.Vector2Norm;
import ch.alpine.tensor.sca.Chop;
import ch.alpine.tensor.sca.Sign;

/** for instance the point {0, 0, ..., 0, 1} with (n + 1) entries is member of H^n */
public enum HnMemberQ implements MemberQ {
  INSTANCE;

  @Override // from MemberQ
  public boolean test(Tensor p) {
    Scalar xn = Last.of(p);
    return Sign.isPositive(xn) //
        && Chop._08.isClose(ratio(p), RealScalar.ONE);
  }

  public static Scalar ratio(Tensor x) {
    Scalar xn = Last.of(x);
    Sign.requirePositive(xn);
    return Vector2Norm.of(Drop.tail(x, 1).append(RealScalar.ONE)).divide(xn);
  }
}
