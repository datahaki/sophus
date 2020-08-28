// code by jph
package ch.ethz.idsc.sophus.hs;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

public abstract class AbstractMemberQ implements MemberQ {
  @Override
  public final Tensor requirePoint(Tensor x) {
    if (isPoint(x))
      return x;
    throw TensorRuntimeException.of(x);
  }

  @Override
  public final Tensor requireTangent(Tensor x, Tensor v) {
    if (isTangent(x, v))
      return v;
    throw TensorRuntimeException.of(x, v);
  }
}
