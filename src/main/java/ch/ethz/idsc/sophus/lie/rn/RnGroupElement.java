// code by jph
package ch.ethz.idsc.sophus.lie.rn;

import java.io.Serializable;

import ch.ethz.idsc.sophus.lie.LieGroupElement;
import ch.ethz.idsc.tensor.Tensor;

/** represents a vector in Euclidean space
 * with addition as group operation
 * 
 * the adjoint map is the identity for each group element */
public class RnGroupElement implements LieGroupElement, Serializable {
  private static final long serialVersionUID = 3526812468106998246L;
  // ---
  private final Tensor coordinate;

  public RnGroupElement(Tensor coordinate) {
    this.coordinate = coordinate;
  }

  @Override // from LieGroupElement
  public Tensor toCoordinate() {
    return coordinate.unmodifiable();
  }

  @Override // from LieGroupElement
  public RnGroupElement inverse() {
    return new RnGroupElement(coordinate.negate());
  }

  @Override // from LieGroupElement
  public Tensor combine(Tensor tensor) {
    return coordinate.add(tensor);
  }

  @Override // from LieGroupElement
  public Tensor adjoint(Tensor tensor) {
    return tensor.copy();
  }

  @Override // from LieGroupElement
  public Tensor dL(Tensor tensor) {
    return tensor.copy();
  }

  @Override // from LieGroupElement
  public Tensor dR(Tensor tensor) {
    return tensor.copy();
  }
}
