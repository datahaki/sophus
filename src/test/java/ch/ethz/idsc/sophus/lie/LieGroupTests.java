// code by jph
package ch.ethz.idsc.sophus.lie;

import ch.ethz.idsc.tensor.Tensor;

public enum LieGroupTests {
  ;
  public static Tensor invert(LieGroup lieGroup, Tensor sequence) {
    return Tensor.of(sequence.stream() //
        .map(lieGroup::element) //
        .map(LieGroupElement::inverse) //
        .map(LieGroupElement::toCoordinate));
  }
}
