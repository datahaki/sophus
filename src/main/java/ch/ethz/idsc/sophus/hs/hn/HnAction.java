// code by jph
package ch.ethz.idsc.sophus.hs.hn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

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
