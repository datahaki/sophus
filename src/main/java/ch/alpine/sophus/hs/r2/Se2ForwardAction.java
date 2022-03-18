// code by jph
package ch.alpine.sophus.hs.r2;

import ch.alpine.sophus.lie.se2.Se2GroupElement;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;
import ch.alpine.tensor.api.TensorUnaryOperator;
import ch.alpine.tensor.sca.tri.Cos;
import ch.alpine.tensor.sca.tri.Sin;

/** Se2ForwardAction is a substitute for the operation:
 * SE2 matrix dot point
 * 
 * Se2ForwardAction is the canonic action of SE2 on R^2.
 * 
 * @see Se2InverseAction
 * @see Se2GroupElement */
/* package */ class Se2ForwardAction implements TensorUnaryOperator {
  private final Scalar px;
  private final Scalar py;
  private final Scalar ca;
  private final Scalar sa;

  public Se2ForwardAction(Tensor xya) {
    px = xya.Get(0);
    py = xya.Get(1);
    Scalar angle = xya.Get(2);
    ca = Cos.FUNCTION.apply(angle);
    sa = Sin.FUNCTION.apply(angle);
  }

  @Override // from TensorUnaryOperator
  public Tensor apply(Tensor tensor) {
    Scalar qx = tensor.Get(0);
    Scalar qy = tensor.Get(1);
    return Tensors.of( //
        px.add(qx.multiply(ca)).subtract(qy.multiply(sa)), //
        py.add(qx.multiply(sa)).add(qy.multiply(ca)));
  }
}
