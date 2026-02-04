package ch.alpine.sophus.hs.st;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.Dot;
import ch.alpine.tensor.api.TensorUnaryOperator;

public record StAction(Tensor u, Tensor v) implements TensorUnaryOperator {
  @Override
  public Tensor apply(Tensor p) {
    return Dot.of(u, p, v);
  }
}
