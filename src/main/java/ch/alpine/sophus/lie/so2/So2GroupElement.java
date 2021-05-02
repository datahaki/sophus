// code by ob
package ch.alpine.sophus.lie.so2;

import ch.alpine.sophus.lie.LieGroupElement;
import ch.alpine.tensor.Scalar;
import ch.alpine.tensor.Tensor;

/** Ethan Eade:
 * "Because rotations in the plane commute, the adjoint of SO(2) is the identity function." */
public class So2GroupElement implements LieGroupElement {
  private final Scalar alpha;

  public So2GroupElement(Scalar alpha) {
    this.alpha = alpha;
  }

  @Override
  public Tensor toCoordinate() {
    return alpha;
  }

  @Override // from LieGroupElement
  public So2GroupElement inverse() {
    return new So2GroupElement(alpha.negate());
  }

  @Override // from LieGroupElement
  public Scalar combine(Tensor tensor) {
    return So2.MOD.apply(alpha.add(tensor));
  }

  @Override // from LieGroupElement
  public Scalar adjoint(Tensor tensor) {
    return (Scalar) tensor;
  }

  @Override // from LieGroupElement
  public Scalar dL(Tensor tensor) {
    return (Scalar) tensor;
  }
}
