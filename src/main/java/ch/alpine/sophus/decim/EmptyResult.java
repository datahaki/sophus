// code by jph
package ch.alpine.sophus.decim;

import ch.alpine.sophus.decim.CurveDecimation.Result;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.Tensors;

/* package */ enum EmptyResult implements Result {
  INSTANCE;

  @Override // from Result
  public Tensor result() {
    return Tensors.empty();
  }

  @Override // from Result
  public Tensor errors() {
    return Tensors.empty();
  }
}
