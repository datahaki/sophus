// code by jph
package ch.alpine.sophus.crv.dub;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;

/* package */ enum Se2Flip implements TensorUnaryOperator {
  FUNCTION;

  @Override
  public Tensor apply(Tensor xya) {
    return Tensors.of(xya.Get(0), xya.Get(1).negate(), xya.Get(2).negate());
  }
}
