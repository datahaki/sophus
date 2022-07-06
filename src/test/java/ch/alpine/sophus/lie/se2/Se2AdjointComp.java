// code by jph
package ch.alpine.sophus.lie.se2;

import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Throw;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;

/* package */ class Se2AdjointComp implements TensorUnaryOperator {
  private final Tensor matrix;

  /** @param element from Lie Group SE2 as coordinates {x, y, omega} */
  public Se2AdjointComp(Tensor xya) {
    if (xya.length() != 3)
      throw Throw.of(xya);
    matrix = Se2Matrix.of(Tensors.of( //
        xya.get(1), // t2
        xya.get(0).negate(), // -t1
        xya.get(2))); // omega
  }

  @Override
  public Tensor apply(Tensor uvw) {
    return matrix.dot(uvw);
  }
}
