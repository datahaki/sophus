// code by jph
package ch.alpine.sophus.hs.s;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.mat.OrthogonalMatrixQ;

/** formula generalized from
 * "Rotation Between Two Vectors in R^3"
 * by Ethan Eade */
public record SnAction(Tensor g) implements TensorUnaryOperator {
  public SnAction {
    OrthogonalMatrixQ.INSTANCE.requireMember(g);
  }

  @Override
  public Tensor apply(Tensor p) {
    return g.dot(p);
  }
}
