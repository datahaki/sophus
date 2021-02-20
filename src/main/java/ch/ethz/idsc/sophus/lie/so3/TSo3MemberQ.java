// code by jph
package ch.ethz.idsc.sophus.lie.so3;

import ch.ethz.idsc.sophus.math.MemberQ;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.TensorRuntimeException;

public class TSo3MemberQ implements MemberQ {
  private final Tensor p;

  public TSo3MemberQ(Tensor p) {
    this.p = p;
  }

  @Override
  public boolean test(Tensor v) {
    throw TensorRuntimeException.of(v); // FIXME
  }
}
