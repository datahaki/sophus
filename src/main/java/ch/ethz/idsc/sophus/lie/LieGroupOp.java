// code by jph
package ch.ethz.idsc.sophus.lie;

import java.io.Serializable;

import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.opt.TensorUnaryOperator;

public final class LieGroupOp implements Serializable {
  private final TensorUnaryOperator tensorUnaryOperator;

  public LieGroupOp(TensorUnaryOperator tensorUnaryOperator) {
    this.tensorUnaryOperator = tensorUnaryOperator;
  }

  public Tensor one(Tensor tensor) {
    return tensorUnaryOperator.apply(tensor);
  }

  public Tensor all(Tensor sequence) {
    return Tensor.of(sequence.stream().map(tensorUnaryOperator));
  }
}