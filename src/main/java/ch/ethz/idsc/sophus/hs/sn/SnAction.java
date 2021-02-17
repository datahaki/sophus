// code by jph
package ch.ethz.idsc.sophus.hs.sn;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.api.TensorUnaryOperator;

/** formula generalized from
 * "Rotation Between Two Vectors in R^3"
 * by Ethan Eade */
public class SnAction implements TensorUnaryOperator {
  private final Tensor g;

  public SnAction(Tensor g) {
    this.g = g;
  }

  @Override
  public Tensor apply(Tensor p) {
    return g.dot(p);
  }
}
