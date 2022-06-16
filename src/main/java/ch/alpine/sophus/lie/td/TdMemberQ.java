// code by jph
package ch.alpine.sophus.lie.td;

import ch.alpine.sophus.api.MemberQ;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Last;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.sca.Sign;

public enum TdMemberQ implements MemberQ {
  INSTANCE;

  @Override
  public boolean test(Tensor t_lambda) {
    VectorQ.require(t_lambda);
    return Sign.isPositive(Last.of(t_lambda));
  }
}
