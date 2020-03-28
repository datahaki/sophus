// code by jph
package ch.ethz.idsc.sophus.crv.decim;

import ch.ethz.idsc.sophus.crv.decim.CurveDecimation.Result;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.Tensors;

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