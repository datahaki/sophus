// code by jph
package ch.alpine.sophus.lie.sc;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;
import ch.alpine.tensor.alg.VectorQ;
import ch.alpine.tensor.red.Times;

public class ScGroupElement implements LieGroupElement {
  /** @param value
   * @return
   * @throws Exception if value is not strictly positive */
  public static ScGroupElement of(Tensor tensor) {
    return new ScGroupElement(VectorQ.requireLength(tensor, 1));
  }

  private final Tensor tensor;

  private ScGroupElement(Tensor tensor) {
    this.tensor = tensor;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return tensor.unmodifiable();
  }

  @Override
  public LieGroupElement inverse() {
    return new ScGroupElement(tensor.map(Scalar::reciprocal));
  }

  @Override
  public Tensor combine(Tensor tensor) {
    return Times.of(this.tensor, tensor);
  }

  @Override
  public Tensor adjoint(Tensor tensor) {
    throw new UnsupportedOperationException();
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    throw new UnsupportedOperationException();
  }
}
