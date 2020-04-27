// code by jph
package ch.ethz.idsc.sophus.lie.sc;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Scalar;
import ch.ethz.idsc.tensor.Tensor;
import ch.ethz.idsc.tensor.alg.VectorQ;

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
    return this.tensor.pmul(tensor);
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
