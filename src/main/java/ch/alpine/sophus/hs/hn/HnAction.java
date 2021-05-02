// code by jph
package ch.alpine.sophus.hs.hn;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;

public class HnAction implements TensorUnaryOperator {
  private final Tensor g;

  public HnAction(Tensor g) {
    this.g = g;
  }

  @Override
  public Tensor apply(Tensor p) {
    return g.dot(p);
  }
}
